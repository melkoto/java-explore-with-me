package ru.practicum.main.category.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryResponseDto {
    private int id;
    private String name;

    public CategoryResponseDto(int id) {
        this.id = id;
    }
}
