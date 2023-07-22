package ru.practicum.main.event.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.event.model.StateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateEventDto {
    @Size(min = 20, max = 2000)
    private String annotation;

    private Integer category;

    @Size(min = 20, max = 7000)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
