package ru.practicum.main.event.service.pub;

import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.error.BadRequestException;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PublicEventServiceImpl implements PublicEventService {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final StatsClient statsClient;
    private final EventRepository eventRepository;

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
