package ru.practicum.main.event.service.admin;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.LocationDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.dto.UpdateEventDto;
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
public class AdminEventServiceImpl implements AdminEventService {
    private final StatsClient statsClient;
    private final AdminEventRepository eventRepository;
    private final LocationRepository locationRepository;

    public AdminEventServiceImpl(StatsClient statsClient, AdminEventRepository eventRepository, LocationRepository locationRepository) {
        this.statsClient = statsClient;
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    public List<FullEventResponseDto> getEvents(List<Long> users, List<String> states, List<Integer> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Integer from, Integer size) {
        List<FullEventResponseDto> events = eventRepository.getEventsFiltered(
                        users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size))
                .stream()
                .map(event -> EventMapper.toEventFullDto(event, null, 0))
                .collect(Collectors.toList());

        return fillViewsForList(events);
    }

    @Override
    public FullEventResponseDto updateEvent(Long eventId, UpdateEventDto eventDto) {
        Event eventEnity = eventRepository.findById(eventId).orElseThrow();
        if (!eventEnity.getPublishedOn().isBefore(eventDto.getEventDate().minusHours(1))) {
            throw new ConflictException("Дата начала изменяемого события должна быть " +
                    "не ранее чем за час от даты публикации.");
        }
        if (eventDto.getStateAction().equals(PUBLISH_EVENT) && !eventEnity.getState().equals(PENDING)) {
            throw new ConflictException("Событие можно публиковать, только если оно в состоянии ожидания публикации");
        }
        if (eventDto.getStateAction().equals(REJECT_EVENT) && eventEnity.getState().equals(PUBLISHED)) {
            throw new ConflictException("Событие можно отклонить, только если оно еще не опубликовано");
        }
        return toEventDto(eventRepository.save(
                updateDtoToEvent(eventDto, eventEnity, saveLocation(eventDto.getLocation()))));
    }

    private <T extends ShortEventResponseDto> List<T> fillViewsForList(List<T> events) {
        List<String> listOfUris = events.stream()
                .map(T::getId)
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

    private Location saveLocation(LocationDto locationDto) {
        if (!locationRepository.existsByLatAndLon(locationDto.getLat(), locationDto.getLon())) {
            return locationRepository.save(dtoToLocation(locationDto));
        }
        return locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
    }
}
