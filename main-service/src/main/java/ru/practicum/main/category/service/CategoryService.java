package ru.practicum.main.category.service;

import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;

public interface CategoryService {
    CategoryResponseDto addCategory(CategoryDto categoryDto);
}
