package ru.practicum.main.event.service.pub;

import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.eventEnums.SortTypes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<ShortEventResponseDto> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, SortTypes sortType, Integer from,
                                          Integer size, HttpServletRequest request);

    FullEventResponseDto getEvent(long eventId, HttpServletRequest request);
}
