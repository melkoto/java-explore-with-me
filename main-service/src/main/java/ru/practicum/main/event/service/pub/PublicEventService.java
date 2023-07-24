package ru.practicum.main.event.service.pub;

import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {
    List<ShortEventResponseDto> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                          String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                          Integer size, HttpServletRequest request);

    FullEventResponseDto getEvent(long eventId, HttpServletRequest request);
}
