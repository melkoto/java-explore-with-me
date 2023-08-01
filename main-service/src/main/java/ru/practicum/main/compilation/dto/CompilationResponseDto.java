package ru.practicum.main.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.event.model.Event;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponseDto {
    private Set<Event> events;

    private Long id;

    private Boolean pinned;

    private String title;
}
