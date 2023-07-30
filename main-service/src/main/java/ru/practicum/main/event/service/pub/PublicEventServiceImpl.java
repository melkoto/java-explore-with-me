package ru.practicum.main.event.service.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.PublicEventRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final StatsClient statsClient;
    private final PublicEventRepository publicEventRepository;

    public PublicEventServiceImpl(StatsClient statsClient, PublicEventRepository publicEventRepository) {
        this.statsClient = statsClient;
        this.publicEventRepository = publicEventRepository;
    }

    public List<ShortEventResponseDto> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                                 String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                                 Integer size, HttpServletRequest request) {
        statsClient.saveHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now());

        LocalDateTime start = (rangeStart != null)
                ? LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
                : LocalDateTime.now();
        LocalDateTime end = (rangeEnd != null)
                ? LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
                : LocalDateTime.now().plusYears(100);

        Pageable pageable = PageRequest.of(from / size, size, Sort.Direction.valueOf(sort));

        Page<Event> events = publicEventRepository.findAllEvents(text, categories, paid, start, end, pageable);

        log.info("events: {}", events);

        return events.getContent().stream()
                .map(event -> EventMapper.toEventShortDto(event, event.getConfirmedRequests(), event.getViews()))
                .collect(Collectors.toList());
    }

    public FullEventResponseDto getEvent(long id, HttpServletRequest request) {
        statsClient.saveHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now());

        Event event = publicEventRepository.findById(id).orElseThrow();

        log.info("event: {}", event);

        return EventMapper.toEventFullDto(event, event.getViews(), event.getConfirmedRequests());
    }
}
