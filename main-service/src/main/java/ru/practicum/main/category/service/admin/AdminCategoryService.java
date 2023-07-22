package ru.practicum.main.category.service.admin;

import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;

public interface AdminCategoryService {
    CategoryResponseDto addCategory(CategoryDto categoryDto);

    void deleteCategory(int catId);

    CategoryResponseDto updateCategory(CategoryDto categoryDto, int catId);
}
