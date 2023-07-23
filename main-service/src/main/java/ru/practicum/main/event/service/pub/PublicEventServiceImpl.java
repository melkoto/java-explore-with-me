package ru.practicum.main.event.service.pub;

import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.error.BadRequestException;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicEventServiceImpl implements PublicEventService {
    private final StatsClient statsClient;
    private final EventRepository eventRepository;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public PublicEventServiceImpl(StatsClient statsClient, EventRepository eventRepository) {
        this.statsClient = statsClient;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<ShortEventResponseDto> getEvents(String text, List<Long> categoriesId, Boolean paid,
                                                 String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                 String sort, int from, int size) {
        if (rangeStart != null && rangeEnd != null) {
            if (LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
                    .isAfter(LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))) {
                throw new BadRequestException("rangeStart must be before rangeEnd");
            }
        }
        return null;
    }
}
