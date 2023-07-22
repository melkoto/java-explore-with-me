package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.user.dto.UserResponseDto;

import java.time.LocalDateTime;

@Data
public class ShortEventResponseDto {
    private String annotation;

    private CategoryResponseDto category;

    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    private UserResponseDto initiator;

    private boolean paid;

    private String title;

    private Long views;
}
