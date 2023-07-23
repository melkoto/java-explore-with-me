package ru.practicum.main.event.service.pub;

import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.repository.EventRepository;

import java.util.List;

@Service
public class PublicEventServiceImpl implements PublicEventService {
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
        return null;
    }
}
