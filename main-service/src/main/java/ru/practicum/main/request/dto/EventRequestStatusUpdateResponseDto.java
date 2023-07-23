package ru.practicum.main.request.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateResponseDto {
    private List<ParticipationRequestDto> confirmedRequests;

    private List<ParticipationRequestDto> rejectedRequests;
}
