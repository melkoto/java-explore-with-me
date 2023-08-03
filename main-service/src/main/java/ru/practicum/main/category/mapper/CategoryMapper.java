package ru.practicum.main.category.mapper;

import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.model.Category;

public class CategoryMapper {
    public static Category mapCategoryDtoToCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    public static CategoryResponseDto mapCategoryToCategoryResponseDto(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());
        return categoryResponseDto;
    }

    public static Category mapCategoryResponseDtoToCategory(CategoryResponseDto categoryResponseDto) {
        Category category = new Category();
        category.setId(categoryResponseDto.getId());
        category.setName(categoryResponseDto.getName());
        return category;
    }

    public static Category mapToCategory(CategoryDto newCategoryDto, Category category) {
        category.setName(newCategoryDto.getName());
        return category;
    }
}
