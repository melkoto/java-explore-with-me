package ru.practicum.main.category.service.publical;

import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.PublicCategoryRepository;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.user.mapper.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.user.mapper.CategoryMapper.mapCategoryToCategoryResponseDto;

@Service
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final PublicCategoryRepository publicCategoryRepository;

    public PublicCategoryServiceImpl(PublicCategoryRepository publicCategoryRepository) {
        this.publicCategoryRepository = publicCategoryRepository;
    }

    @Override
    public CategoryResponseDto getCategory(int catId) {
        Category category = publicCategoryRepository
                .findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id: " + catId + " doesn" + "'t exist"));
        return mapCategoryToCategoryResponseDto(category);
    }

    @Override
    public List<CategoryResponseDto> getCategories(Integer from, Integer size) {
        List<Category> categories = publicCategoryRepository.findAll();

        return categories.stream()
                .skip(from)
                .limit(size)
                .map(CategoryMapper::mapCategoryToCategoryResponseDto)
                .collect(Collectors.toList());
    }
}