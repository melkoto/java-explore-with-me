package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.user.dto.UserResponseDto;

import java.time.LocalDateTime;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@Data
public class ShortEventResponseDto {
    private Long id;

    private String annotation;

    private CategoryResponseDto category;

    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private UserResponseDto initiator;

    private boolean paid;

    private String title;

    private Long views;
}
