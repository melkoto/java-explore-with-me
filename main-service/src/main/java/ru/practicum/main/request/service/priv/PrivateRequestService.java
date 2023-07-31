package ru.practicum.main.request.service.priv;

import ru.practicum.main.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    public ParticipationRequestDto createRequest(Long userId, Long eventId);

    public List<ParticipationRequestDto> getRequests(Long userId);

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId);
}
