package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.main.event.model.Location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class CreateEventDto {
    @NotEmpty
    @Size(min = 20, max = 2000, message = "Annotation must be between 20 and 2000 characters")
    private String annotation;

    @NotEmpty
    private int category;

    @NotEmpty
    @Size(min = 20, max = 7000, message = "Description must be between 20 and 7000 characters")
    private String description;

    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;

    @NotEmpty
    private Location location;

    @NotEmpty
    @Size(min = 3, max = 120, message = "Title must be between 3 and 120 characters")
    private String title;


    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;
}
