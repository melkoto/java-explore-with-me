package ru.practicum.main.event.service.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.category.repository.AdminCategoryRepository;
import ru.practicum.main.error.BadRequestException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.eventEnums.SortTypes;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.PublicEventRepository;
import ru.practicum.main.request.repository.PrivateRequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.PUBLISHED;
import static ru.practicum.main.event.mapper.EventMapper.toEventFullDto;

@Service
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final StatsClient statsClient;
    private final PublicEventRepository publicEventRepository;
    private final AdminCategoryRepository categoryRepository;
    private final PrivateRequestRepository requestRepository;
    private final PublicEventRepository eventRepository;

    public PublicEventServiceImpl(StatsClient statsClient, PublicEventRepository publicEventRepository, AdminCategoryRepository categoryRepository, PrivateRequestRepository requestRepository, PublicEventRepository eventRepository) {
        this.statsClient = statsClient;
        this.publicEventRepository = publicEventRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
    }

    public List<ShortEventResponseDto> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                                 String rangeEnd, Boolean onlyAvailable, SortTypes sortType, Integer from,
                                                 Integer size, HttpServletRequest request) {
        statsClient.saveHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        LocalDateTime start = (rangeStart != null)
                ? LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
                : LocalDateTime.now();
        LocalDateTime end = (rangeEnd != null)
                ? LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
                : LocalDateTime.now().plusYears(100);

        if (end.isBefore(start)) {
            throw new BadRequestException("rangeEnd must be after rangeStart");
        }

        Sort sort;
        switch (sortType) {
            case VIEWS:
                sort = Sort.by(Sort.Direction.DESC, "views");
                break;
            case EVENT_DATE:
            default:
                sort = Sort.by(Sort.Direction.ASC, "eventDate");
                break;
        }

        Pageable pageable = PageRequest.of(from / size, size, sort);

        Page<Event> events = null;
        if (sortType == SortTypes.VIEWS) {
            events = publicEventRepository.findAllEventsOrderByViews(text, categories, paid, start, end, pageable);
            log.info("VIEW Page<Event>: {}", events);
        } else if (sortType == SortTypes.EVENT_DATE) {
            events = publicEventRepository.findAllEventsOrderByEventDate(text, categories, paid, start, end, pageable);
            log.info("EVENT_DATE Page<Event>: {}", events);
        }

        log.info("events for public: {}", events);

        return events.getContent().stream()
                .map(event -> EventMapper.toEventShortDto(event, event.getConfirmedRequests(), event.getViews()))
                .collect(Collectors.toList());
    }

    public FullEventResponseDto getEvent(long eventId, HttpServletRequest request) {
        statsClient.saveHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now());

        Event event = publicEventRepository.findById(eventId).orElseThrow();

        if (event.getState() != PUBLISHED) {
            throw new NotFoundException("There is no published event with id: " + eventId);
        }

        log.info("event: {}", event);

        return toEventFullDto(event, event.getViews(), event.getConfirmedRequests());
    }
}
