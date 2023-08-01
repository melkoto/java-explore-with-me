package ru.practicum.main.compilation.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class CreateCompilationDto {
    private Set<Long> events;

    private Boolean pinned;

    @NotEmpty
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;
}
