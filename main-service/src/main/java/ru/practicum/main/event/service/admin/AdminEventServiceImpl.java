package ru.practicum.main.event.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.LocationDto;
import ru.practicum.main.event.dto.UpdateEventDto;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.event.repository.AdminEventRepository;
import ru.practicum.main.event.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.PENDING;
import static ru.practicum.main.event.eventEnums.State.PUBLISHED;
import static ru.practicum.main.event.eventEnums.StateAction.PUBLISH_EVENT;
import static ru.practicum.main.event.eventEnums.StateAction.REJECT_EVENT;
import static ru.practicum.main.event.mapper.EventMapper.toEventDto;
import static ru.practicum.main.event.mapper.EventMapper.updateDtoToEvent;
import static ru.practicum.main.event.mapper.LocationMapper.dtoToLocation;

@Service
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {
    private final StatsClient statsClient;
    private final AdminEventRepository eventRepository;
    private final LocationRepository locationRepository;

    public AdminEventServiceImpl(StatsClient statsClient, AdminEventRepository eventRepository, LocationRepository locationRepository) {
        this.statsClient = statsClient;
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    public List<FullEventResponseDto> getEvents(List<Long> users, List<State> states, List<Integer> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Integer from, Integer size) {
        List<FullEventResponseDto> events = eventRepository.getEventsFiltered(
                        users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size))
                .stream()
                .map(event -> EventMapper.toEventFullDto(event, null, 0))
                .collect(Collectors.toList());

        List<String> listOfUris = events.stream()
                .map(FullEventResponseDto::getId)
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
    public FullEventResponseDto updateEvent(Long eventId, UpdateEventDto eventDto) {
        Event eventEntity = eventRepository.findById(eventId).orElseThrow(() ->
                new ConflictException("Событие с id " + eventId + " не найдено."));

        log.info("Event entity before update: {}", eventEntity);

        if (eventEntity.getState().equals(PUBLISHED)) {
            throw new ConflictException("Can not update published event.");
        }

        log.info("Event entity: {}", eventEntity);

        if (eventDto.getEventDate() != null &&
                !eventEntity.getPublishedOn().isBefore(eventDto.getEventDate().minusHours(1))) {
            // translate line below to english
            throw new ConflictException("Date of event must be at least one hour after publication");
        }

        if (eventDto.getStateAction().equals(PUBLISH_EVENT) && !eventEntity.getState().equals(PENDING)) {
            throw new ConflictException("Event can be published only if it is in pending state");
        }

        if (eventDto.getStateAction().equals(REJECT_EVENT) && eventEntity.getState().equals(PUBLISHED)) {
            throw new ConflictException("Event can not be rejected if it is in published state");
        }

        eventEntity.setState(PUBLISHED);

        FullEventResponseDto fullEventResponseDto = toEventDto(eventRepository.save(
                updateDtoToEvent(eventDto, eventEntity,
                        eventDto.getLocation() == null
                                ? eventEntity.getLocation()
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
}