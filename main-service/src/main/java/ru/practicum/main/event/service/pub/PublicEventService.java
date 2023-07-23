package ru.practicum.main.event.service.pub;

import ru.practicum.main.event.dto.ShortEventResponseDto;

import java.util.List;

public interface PublicEventService {
    List<ShortEventResponseDto> getEvents(String text, List<Long> categoriesId, Boolean paid, String rangeStart,
                                          String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);
}
