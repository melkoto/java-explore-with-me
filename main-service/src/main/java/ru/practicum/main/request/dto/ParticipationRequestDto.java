package ru.practicum.main.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.main.request.enums.Status;

import java.time.LocalDateTime;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@Data
public class ParticipationRequestDto {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime created;

    private Long event;

    private Long requester;

    private Status status;
}
