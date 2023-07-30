package ru.practicum.main.event.service.priv;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.event.repository.LocationRepository;
import ru.practicum.main.event.repository.PrivateEventRepository;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.CANCELED;
import static ru.practicum.main.event.mapper.EventMapper.*;
import static ru.practicum.main.event.mapper.LocationMapper.dtoToLocation;

@Service
public class PrivateEventServiceImpl implements PrivateEventService {
    private final AdminUserRepository userRepository;
    private final PrivateEventRepository eventRepository;
    private final StatsClient statsClient;

    private final LocationRepository locationRepository;

    public PrivateEventServiceImpl(AdminUserRepository userRepository, PrivateEventRepository eventRepository, StatsClient statsClient, LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.statsClient = statsClient;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<ShortEventResponseDto> getEvents(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        // TODO add confirmed requests and views
        List<ShortEventResponseDto> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size))
                .stream()
                .map(event -> toEventShortDto(event, null, 0))
                .collect(Collectors.toList());
        return fillViewsForList(events);
    }

    @Override
    public FullEventResponseDto addEvent(CreateEventDto createEventDto, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        if (createEventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента");
        }
        return fillViewsForList(List.of(toEventFullDto(eventRepository.save(
                createDtoToEvent(createEventDto, saveLocation(createEventDto.getLocation()))), 0L, 0))).get(0);
    }

    @Override
    public FullEventResponseDto getEvent(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        return fillViewsForList(List.of(toEventDto(
                eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow()))).get(0);
    }

    @Override
    public FullEventResponseDto updateEvent(UpdateEventUserRequestDto updateEventUserRequestDto, Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getState().equals(CANCELED) || !event.getRequestModeration()) {
            throw new ConflictException("Изменить можно только отмененные события " +
                    "или события в состоянии ожидания модерации");
        }
        if (updateEventUserRequestDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента ");
        }
        return fillViewsForList(List.of(toEventDto(eventRepository.save(
                updateDtoToEvent(updateEventUserRequestDto, event,
                        saveLocation(updateEventUserRequestDto.getLocation())))))).get(0);
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
