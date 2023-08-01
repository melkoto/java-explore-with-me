package ru.practicum.main.request.dto;

import lombok.Data;
import ru.practicum.main.request.enums.Status;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequestDto {
    private List<Long> requestIds;

    private Status status;
}
