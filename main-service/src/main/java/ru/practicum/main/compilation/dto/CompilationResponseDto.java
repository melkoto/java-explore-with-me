package ru.practicum.main.compilation.dto;

import lombok.Data;
import ru.practicum.main.event.dto.ShortEventResponseDto;

import java.util.List;

@Data
public class CompilationResponseDto {
    private List<ShortEventResponseDto> events;

    private Long id;

    private Boolean pinned;

    private String title;
}
