package ru.practicum.main.event.dto;

import lombok.Data;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.event.model.State;
import ru.practicum.main.user.dto.UserResponseDto;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;

@Data
public class EventResponseDto {
    private Long id;

    private String annotation;

    private CategoryResponseDto category;

    private Integer confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    private UserResponseDto initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    private String title;

    private Integer views;
}
