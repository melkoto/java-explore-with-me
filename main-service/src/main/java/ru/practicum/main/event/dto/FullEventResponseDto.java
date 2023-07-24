package ru.practicum.main.event.dto;

import lombok.Data;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.event.EventEnums.State;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.user.dto.UserResponseDto;

import java.time.LocalDateTime;

@Data
public class FullEventResponseDto {
    private Long id;

    private String annotation;

    private CategoryResponseDto category;

    private long confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    private UserResponseDto initiator;

    private Location location;

    private boolean paid;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private State state;

    private String title;

    private long views;
}
