package ru.practicum.main.event.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.LocationDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.dto.UpdateEventDto;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.event.eventEnums.StateAction;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.event.repository.AdminEventRepository;
import ru.practicum.main.event.repository.LocationRepository;
import ru.practicum.main.request.repository.PrivateRequestRepository;
import ru.practicum.main.utils.EventUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.PENDING;
import static ru.practicum.main.event.eventEnums.State.PUBLISHED;
import static ru.practicum.main.event.eventEnums.StateAction.PUBLISH_EVENT;
import static ru.practicum.main.event.mapper.EventMapper.toEventDto;
import static ru.practicum.main.event.mapper.EventMapper.updateDtoToEvent;
import static ru.practicum.main.event.mapper.LocationMapper.dtoToLocation;
import static ru.practicum.main.request.enums.Status.CONFIRMED;

@Service
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {
    private final AdminEventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventUtils eventUtils;
    private final PrivateRequestRepository requestRepository;

    public AdminEventServiceImpl(AdminEventRepository eventRepository, LocationRepository locationRepository, EventUtils eventUtils, PrivateRequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.eventUtils = eventUtils;
        this.requestRepository = requestRepository;
    }

    public List<FullEventResponseDto> getEvents(Set<Long> users, List<State> states, List<Integer> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Integer from, Integer size) {

        List<FullEventResponseDto> events = eventRepository.getFilteredEvents(
                        users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size))
                .stream()
                .map(EventMapper::toEventDto)
                .map(this::setConfirmedRequests)
                .collect(Collectors.toList());

        log.info("Events after filtering: {}", events);

        return eventUtils.fillViews(events);
    }

    @Override
    public FullEventResponseDto updateEvent(Long eventId, UpdateEventDto eventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ConflictException("Event with id " + eventId + " not found."));

        log.info("Event entity before update: {}", event);

        if (event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Can not update published event.");
        }

        if (eventDto.getStateAction() != null &&
                eventDto.getStateAction().equals(PUBLISH_EVENT) &&
                !event.getState().equals(PENDING)) {
            throw new ConflictException("Event can be published only if it is in PENDING state");
        }

        if (eventDto.getStateAction() != null && eventDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            LocalDateTime now = LocalDateTime.now();

            if (eventDto.getEventDate() != null && !now.isBefore(eventDto.getEventDate().minusHours(1))) {
                throw new ConflictException("Publication date must be at least 1 hour before event date.");
            }

            event.setState(PUBLISHED);
            event.setPublishedOn(now);
        } else if (eventDto.getStateAction() != null && eventDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
            event.setState(State.CANCELED);
        }

        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getDescription() != null && !eventDto.getDescription().isBlank()) {
            event.setDescription(eventDto.getDescription());
        }

//        event.setEventDate(Objects.requireNonNullElse(eventDto.getEventDate(), event.getEventDate()));
        event.setPaid(Objects.requireNonNullElse(eventDto.getPaid(), event.getPaid()));
        event.setParticipantLimit(Objects.requireNonNullElse(eventDto.getParticipantLimit(), event.getParticipantLimit()));

        if (eventDto.getTitle() != null && !eventDto.getTitle().isBlank()) {
            event.setTitle(eventDto.getTitle());
        }

        FullEventResponseDto fullEventResponseDto = toEventDto(eventRepository.save(
                updateDtoToEvent(eventDto, event,
                        eventDto.getLocation() == null
                                ? event.getLocation()
                                : saveLocation(eventDto.getLocation()))));

        log.info("Event full response dto after update: {}", fullEventResponseDto);

        return fullEventResponseDto;
    }

    private Location saveLocation(LocationDto locationDto) {
        if (!locationRepository.existsByLatAndLon(locationDto.getLat(), locationDto.getLon())) {
            return locationRepository.save(dtoToLocation(locationDto));
        }
        return locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
    }

    private <T extends ShortEventResponseDto> T setConfirmedRequests(T event) {
        event.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(event.getId(), CONFIRMED)
        );
        return event;
    }
}
