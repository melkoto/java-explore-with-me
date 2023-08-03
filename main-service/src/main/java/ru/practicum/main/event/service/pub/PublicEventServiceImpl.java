package ru.practicum.main.event.service.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.error.BadRequestException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.eventEnums.SortTypes;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.PublicEventRepository;
import ru.practicum.main.request.repository.PrivateRequestRepository;
import ru.practicum.main.utils.EventUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.PUBLISHED;
import static ru.practicum.main.event.mapper.EventMapper.toEventFullDto;
import static ru.practicum.main.request.enums.Status.CONFIRMED;

@Service
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {
    private final StatsClient statsClient;
    private final PublicEventRepository publicEventRepository;
    private final PrivateRequestRepository requestRepository;
    private final PublicEventRepository eventRepository;
    private final EventUtils eventUtils;

    public PublicEventServiceImpl(StatsClient statsClient, PublicEventRepository publicEventRepository, PrivateRequestRepository requestRepository,
                                  PublicEventRepository eventRepository, EventUtils eventUtils) {
        this.statsClient = statsClient;
        this.publicEventRepository = publicEventRepository;
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.eventUtils = eventUtils;
    }

    public List<ShortEventResponseDto> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd, Boolean onlyAvailable, SortTypes sortType, Integer from,
                                                 Integer size, HttpServletRequest request) {
        statsClient.saveHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        if (rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Range start date is after range end date");
        }

        Pageable pageable;
        switch (sortType) {
            case VIEWS:
                pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "views"));
                break;
            case EVENT_DATE:
                pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "eventDate"));
                break;
            default:
                throw new BadRequestException("Sort type is not supported");
        }

        List<Event> events = (onlyAvailable
                ? eventRepository.getAvailableEvents(pageable, text, categories, paid, rangeStart, rangeEnd)
                : eventRepository.getEvents(pageable, text, categories, paid, rangeStart, rangeEnd)).toList();

        return eventUtils.fillViews(events.stream()
                .map(EventMapper::toEventShortDto)
                .map(this::setConfirmedRequests)
                .collect(Collectors.toList()));
    }

    public FullEventResponseDto getEvent(long eventId, HttpServletRequest request) {
        statsClient.saveHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now());

        Event event = publicEventRepository.findById(eventId).orElseThrow();

        if (event.getState() != PUBLISHED) {
            throw new NotFoundException("There is no published event with id: " + eventId);
        }

        log.info("event: {}", event);

        return eventUtils.setViews(toEventFullDto(event, event.getViews(),
                event.getConfirmedRequests()));
    }

    private <T extends ShortEventResponseDto> T setConfirmedRequests(T event) {
        event.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(event.getId(), CONFIRMED)
        );
        return event;
    }
}
