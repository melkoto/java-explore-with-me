package ru.practicum.main.category.service;

import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;

import static ru.practicum.main.user.mapper.CategoryMapper.mapCategoryDtoToCategory;
import static ru.practicum.main.user.mapper.CategoryMapper.mapCategoryToCategoryResponseDto;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponseDto addCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(mapCategoryDtoToCategory(categoryDto));
        return mapCategoryToCategoryResponseDto(category);
    }
}
