package ru.practicum.main.event.service.admin;

import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.UpdateEventDto;
import ru.practicum.main.event.eventEnums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface AdminEventService {
    List<FullEventResponseDto> getEvents(Set<Long> users, List<State> states, List<Integer> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    FullEventResponseDto updateEvent(Long eventId, UpdateEventDto event);
}
