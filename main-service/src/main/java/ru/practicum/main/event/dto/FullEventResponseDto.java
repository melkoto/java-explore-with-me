package ru.practicum.main.event.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.main.event.eventEnums.State;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class FullEventResponseDto extends ShortEventResponseDto {
    private LocalDateTime createdOn;

    private String description;

    private LocationDto location;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private State state;
}
