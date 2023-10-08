package ru.practicum.main.category.service.admin;

import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;

public interface AdminCategoryService {
    //TODO Есть много вопросов к созданию интерфейсов которые используются только одним классом. Холиварная тема.
    CategoryResponseDto addCategory(CategoryDto categoryDto);

    void deleteCategory(int catId);

    CategoryResponseDto updateCategory(CategoryDto categoryDto, Integer catId);
}
