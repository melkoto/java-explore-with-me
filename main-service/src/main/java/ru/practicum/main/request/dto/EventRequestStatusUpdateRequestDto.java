package ru.practicum.main.request.dto;

import lombok.Data;
import ru.practicum.main.event.model.State;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequestDto {
    private List<Long> requestIds;

    private State status;
}
