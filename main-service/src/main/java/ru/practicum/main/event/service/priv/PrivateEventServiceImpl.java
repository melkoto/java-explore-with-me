package ru.practicum.main.event.service.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.error.BadRequestException;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.event.eventEnums.StateAction;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.event.repository.LocationRepository;
import ru.practicum.main.event.repository.PrivateEventRepository;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResponseDto;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.enums.Status;
import ru.practicum.main.request.mapper.RequestMapper;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.repository.PrivateRequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.*;
import static ru.practicum.main.event.eventEnums.StateAction.*;
import static ru.practicum.main.event.mapper.EventMapper.*;
import static ru.practicum.main.event.mapper.LocationMapper.dtoToLocation;
import static ru.practicum.main.event.mapper.LocationMapper.locationToDto;
import static ru.practicum.main.request.enums.Status.CONFIRMED;
import static ru.practicum.main.request.mapper.RequestMapper.toEventRequestStatusUpdateResult;
import static ru.practicum.main.request.mapper.RequestMapper.toParticipationRequestDto;

@Service
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService {
    private final AdminUserRepository userRepository;
    private final PrivateEventRepository eventRepository;
    private final StatsClient statsClient;

    private final LocationRepository locationRepository;
    private final PrivateRequestRepository requestRepository;

    public PrivateEventServiceImpl(AdminUserRepository userRepository, PrivateEventRepository eventRepository,
                                   StatsClient statsClient, LocationRepository locationRepository,
                                   PrivateRequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.statsClient = statsClient;
        this.locationRepository = locationRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<ShortEventResponseDto> getEvents(Long userId, Integer from, Integer size) {
        validateUser(userId);

        List<ShortEventResponseDto> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size))
                .stream()
                .map(event -> toEventShortDto(event,
                        requestRepository.countByEventIdAndStatus(event.getId(), CONFIRMED), 0L))
                .collect(Collectors.toList());

        List<String> listOfUris = events.stream()
                .map(ShortEventResponseDto::getId)
                .map(Object::toString)
                .map(s -> "/events/" + s)
                .collect(Collectors.toList());

        Object bodyWithViews = statsClient.getAllStats(listOfUris).getBody();

        return events.stream()
                .peek(event -> {
                    if (bodyWithViews instanceof LinkedHashMap) {
                        Object view = ((LinkedHashMap<?, ?>) bodyWithViews).get(event.getId().toString());
                        if (view != null) {
                            event.setViews(Long.parseLong(view.toString()));
                        } else {
                            event.setViews(0L);
                        }
                    }
                })
                .collect(Collectors.toList());

    }

    @Override
    public FullEventResponseDto addEvent(CreateEventDto createEventDto, Long userId) {
        validateUser(userId);

        if (createEventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {

            throw new BadRequestException("The event date and time should not be " +
                    "less than two hours from the current moment." + " Start time: " + createEventDto.getEventDate());
        }

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        Event event = createDtoToEvent(createEventDto, saveLocation(createEventDto.getLocation()), user);
        event.setConfirmedRequests(requestRepository.countByEventIdAndStatus(event.getId(), CONFIRMED));

        return toEventDto(eventRepository.save(event));
    }

    @Override
    public FullEventResponseDto getEvent(Long userId, Long eventId) {
        validateUser(userId);

        return toEventDto(
                eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow());
    }

    @Override
    public FullEventResponseDto updateEvent(UpdateEventUserRequestDto updateEventUserRequestDto, Long userId, Long eventId) {
        validateUser(userId);

        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException("Event with id " + eventId + " not found"));

        log.info("Event in private: {}", event);

        if (event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Published events cannot be modified");
        }

        if (!event.getState().equals(CANCELED) && !event.getState().equals(PENDING)) {
            throw new ConflictException("Only cancelled events or events in moderation can be modified");
        }

        if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("The event date and time should not be less than two hours from the current moment");
        }

        if (PUBLISH_EVENT.equals(updateEventUserRequestDto.getStateAction())) {
            event.setState(PUBLISHED);
        } else if (REJECT_EVENT.equals(updateEventUserRequestDto.getStateAction())) {
            event.setState(State.REJECTED);
        } else if (SEND_TO_REVIEW.equals(updateEventUserRequestDto.getStateAction())) {
            event.setState(State.PENDING);
        } else if (StateAction.CANCEL_REVIEW.equals(updateEventUserRequestDto.getStateAction())) {
            event.setState(State.CANCELED);
        }

        if (updateEventUserRequestDto.getTitle() != null) {
            event.setTitle(updateEventUserRequestDto.getTitle());
        }

        FullEventResponseDto eventDto = toEventDto(eventRepository.save(
                updateDtoToEvent(updateEventUserRequestDto, event,
                        saveLocation(updateEventUserRequestDto.getLocation() == null
                                ? locationToDto(event.getLocation())
                                : updateEventUserRequestDto.getLocation()))));

        log.info("Event in private after update: {}", eventDto);

        return eventDto;
    }

    @Override
    public EventRequestStatusUpdateResponseDto updateEventRequest(
            EventRequestStatusUpdateRequestDto statusUpdateRequest, Long userId, Long eventId) {
        validateUser(userId);

        Event event = eventRepository.getReferenceById(eventId);

        Integer participantLimit = eventRepository.findById(eventId).orElseThrow().getParticipantLimit();
        Integer acceptedRequestCount = requestRepository.countByEventIdAndStatus(eventId, CONFIRMED);

        if (participantLimit < acceptedRequestCount + statusUpdateRequest.getRequestIds().size()) {
            throw new ConflictException("The number of participants cannot exceed the limit. " +
                    "The limit is " + participantLimit + " participants. " +
                    "The number of confirmed requests is " + acceptedRequestCount + " participants.");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (Long id : statusUpdateRequest.getRequestIds()) {
            Request request = requestRepository.getReferenceById(id);
            switch (statusUpdateRequest.getStatus()) {
                case REJECTED:
                    processRejectedRequest(request, rejectedRequests);
                    break;
                case CONFIRMED:
                    processConfirmedRequest(request, confirmedRequests, rejectedRequests, event);
                    break;
            }
        }

        return toEventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        validateUser(userId);

        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id = %d not found.", eventId)));

        List<Request> requests = requestRepository.findAllByEventAndStatus(event, Status.PENDING);

        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private void processRejectedRequest(Request request, List<ParticipationRequestDto> rejectedRequests) {
        if (CONFIRMED.equals(request.getStatus())) {
            throw new ConflictException("Already confirmed request cannot be rejected");
        }

        request.setStatus(Status.REJECTED);
        rejectedRequests.add(toParticipationRequestDto(request));
        requestRepository.save(request);
    }

    private void processConfirmedRequest(Request request, List<ParticipationRequestDto> confirmedRequests,
                                         List<ParticipationRequestDto> rejectedRequests, Event event) {
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            log.warn("The participant limit has been reached for event with id={}!", event.getId());
            request.setStatus(Status.REJECTED);
            rejectedRequests.add(toParticipationRequestDto(request));
            requestRepository.save(request);
        } else {
            if (!Status.PENDING.equals(request.getStatus())) {
                throw new ConflictException("Request must have status PENDING");
            }

            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            request.setStatus(CONFIRMED);
            confirmedRequests.add(toParticipationRequestDto(request));
            requestRepository.save(request);
        }
    }

    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    private Location saveLocation(LocationDto locationDto) {
        if (!locationRepository.existsByLatAndLon(locationDto.getLat(), locationDto.getLon())) {
            return locationRepository.save(dtoToLocation(locationDto));
        }
        return locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
    }
}
