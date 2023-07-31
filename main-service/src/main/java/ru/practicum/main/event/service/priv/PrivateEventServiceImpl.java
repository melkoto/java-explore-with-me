package ru.practicum.main.event.service.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.category.repository.AdminCategoryRepository;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.event.repository.LocationRepository;
import ru.practicum.main.event.repository.PrivateEventRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.CANCELED;
import static ru.practicum.main.event.eventEnums.State.PENDING;
import static ru.practicum.main.event.mapper.EventMapper.*;
import static ru.practicum.main.event.mapper.LocationMapper.dtoToLocation;
import static ru.practicum.main.event.mapper.LocationMapper.locationToDto;

@Service
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService {
    private final AdminUserRepository userRepository;
    private final PrivateEventRepository eventRepository;
    private final StatsClient statsClient;
    private final AdminCategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    public PrivateEventServiceImpl(AdminUserRepository userRepository, PrivateEventRepository eventRepository, StatsClient statsClient, AdminCategoryRepository categoryRepository, LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.statsClient = statsClient;
        this.categoryRepository = categoryRepository;
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
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        if (createEventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента");
        }

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        return toEventFullDto(eventRepository
                .save(createDtoToEvent(
                        createEventDto, saveLocation(
                                createEventDto.getLocation()), user)), 0L, 0);
    }

    @Override
    public FullEventResponseDto getEvent(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        return toEventDto(
                eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow());
    }

    @Override
    public FullEventResponseDto updateEvent(UpdateEventUserRequestDto updateEventUserRequestDto, Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id " + eventId + " not found"));

        log.info("Event in private: {}", event);

        if (!event.getState().equals(CANCELED) && !event.getState().equals(PENDING)) {
            throw new ConflictException("Only cancelled events or events in moderation can be modified");
        }

        if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("The event date and time should not be less than two hours from the current moment");
        }

        return toEventDto(eventRepository.save(
                updateDtoToEvent(updateEventUserRequestDto, event,
                        saveLocation(updateEventUserRequestDto.getLocation() == null
                                ? locationToDto(event.getLocation())
                                : updateEventUserRequestDto.getLocation()))));
    }


    private Location saveLocation(LocationDto locationDto) {
        if (!locationRepository.existsByLatAndLon(locationDto.getLat(), locationDto.getLon())) {
            return locationRepository.save(dtoToLocation(locationDto));
        }
        return locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
    }
}
