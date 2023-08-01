package ru.practicum.main.event.service.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.category.repository.AdminCategoryRepository;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.event.repository.LocationRepository;
import ru.practicum.main.event.repository.PrivateEventRepository;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResponseDto;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.mapper.RequestMapper;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.repository.PrivateRequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.*;
import static ru.practicum.main.event.mapper.EventMapper.*;
import static ru.practicum.main.event.mapper.LocationMapper.dtoToLocation;
import static ru.practicum.main.event.mapper.LocationMapper.locationToDto;
import static ru.practicum.main.request.mapper.RequestMapper.toEventRequestStatusUpdateResult;

@Service
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService {
    private final AdminUserRepository userRepository;
    private final PrivateEventRepository eventRepository;
    private final StatsClient statsClient;
    private final AdminCategoryRepository categoryRepository;

    private final LocationRepository locationRepository;
    private final PrivateRequestRepository requestRepository;

    public PrivateEventServiceImpl(AdminUserRepository userRepository, PrivateEventRepository eventRepository, StatsClient statsClient, AdminCategoryRepository categoryRepository, LocationRepository locationRepository, PrivateRequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.statsClient = statsClient;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<ShortEventResponseDto> getEvents(Long userId, Integer from, Integer size) {
        validateUser(userId);

        // TODO add confirmed requests and views
        List<ShortEventResponseDto> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size))
                .stream()
                .map(event -> toEventShortDto(event, null, 0L))
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
                        event.setViews(Long.parseLong(((LinkedHashMap<?, ?>) bodyWithViews).get(event.getId()
                                .toString()).toString()));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public FullEventResponseDto addEvent(CreateEventDto createEventDto, Long userId) {
        validateUser(userId);

        if (createEventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("The event date and time should not be less than two hours from the current moment");
        }

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        return toEventDto(eventRepository
                .save(createDtoToEvent(
                        createEventDto, saveLocation(
                                createEventDto.getLocation()), user)));
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
            EventRequestStatusUpdateRequestDto requestDto, Long userId, Long eventId) {
        validateUser(userId);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id = %d not found.", eventId)));

        List<Request> requests = requestRepository.findAllByIdInAndEventId(requestDto.getRequestIds(), eventId);

        requests = updateRequestStatus(requests, event, requestDto);

        Map<State, List<ParticipationRequestDto>> requestsByStatus = getRequestsByStatus(requests);

        log.info("Event request status updated. Event id = {}, user id = {}, status = {}", eventId, userId,
                requestDto.getStatus());

        return toEventRequestStatusUpdateResult(requestsByStatus.get(PUBLISHED), requestsByStatus.get(REJECTED));
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        validateUser(userId);

        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id = %d not found.", eventId)));

        List<Request> requests = requestRepository.findAllByEventAndStatus(event, PENDING);

        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    private List<Request> updateRequestStatus(List<Request> requests, Event event,
                                              EventRequestStatusUpdateRequestDto requestDto) {
        return requests.stream()
                .filter(request -> shouldUpdateStatus(request, event))
                .peek(request -> request.setStatus(requestDto.getStatus()))
                .map(requestRepository::save)
                .collect(Collectors.toList());
    }

    private boolean shouldUpdateStatus(Request request, Event event) {
        return !request.getStatus().equals(PUBLISHED) &&
                (event.getParticipantLimit() > 0 || event.getRequestModeration()) &&
                request.getStatus().equals(REJECTED);
    }

    private Map<State, List<ParticipationRequestDto>> getRequestsByStatus(List<Request> requests) {
        return requests.stream()
                .collect(Collectors.groupingBy(Request::getStatus,
                        Collectors.mapping(RequestMapper::toParticipationRequestDto, Collectors.toList())));
    }

    private Location saveLocation(LocationDto locationDto) {
        if (!locationRepository.existsByLatAndLon(locationDto.getLat(), locationDto.getLon())) {
            return locationRepository.save(dtoToLocation(locationDto));
        }
        return locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
    }
}
