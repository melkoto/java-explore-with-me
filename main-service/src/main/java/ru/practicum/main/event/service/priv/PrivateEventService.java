package ru.practicum.main.event.service.priv;

import ru.practicum.main.event.dto.CreateEventDto;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.dto.UpdateEventUserRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResponseDto;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {
    List<ShortEventResponseDto> getEvents(Long userId, Integer from, Integer size);

    FullEventResponseDto addEvent(CreateEventDto createEventDto, Long userId);

    FullEventResponseDto getEvent(Long userId, Long eventId);

    FullEventResponseDto updateEvent(UpdateEventUserRequestDto updateEventUserRequestDto, Long userId, Long eventId);

    EventRequestStatusUpdateResponseDto updateEventRequest(EventRequestStatusUpdateRequestDto dto, Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);
}
