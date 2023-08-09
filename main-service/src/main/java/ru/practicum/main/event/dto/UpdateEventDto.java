package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.main.event.eventEnums.StateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@Data
public class UpdateEventDto {
    @Size(min = 20, max = 2000, message = "Annotation must be between 20 and 2000 characters")
    private String annotation;

    private Integer category;

    @Size(min = 20, max = 7000, message = "Description must be between 20 and 7000 characters")
    private String description;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Size(min = 3, max = 120, message = "Title must be between 3 and 120 characters")
    private String title;
}
