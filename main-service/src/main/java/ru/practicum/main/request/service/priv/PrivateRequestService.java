package ru.practicum.main.request.service.priv;

import ru.practicum.main.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId);
}
