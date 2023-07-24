package ru.practicum.main.event.service.priv;

import ru.practicum.main.event.dto.ShortEventResponseDto;

import java.util.List;

public interface PrivateEventService {
    List<ShortEventResponseDto> getEvents(Long userId, Integer from, Integer size);
}
