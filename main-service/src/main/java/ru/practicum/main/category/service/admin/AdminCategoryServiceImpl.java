package ru.practicum.main.category.service.admin;

import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.AdminCategoryRepository;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;

import static ru.practicum.main.user.mapper.CategoryMapper.mapCategoryDtoToCategory;
import static ru.practicum.main.user.mapper.CategoryMapper.mapCategoryToCategoryResponseDto;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final AdminCategoryRepository adminCategoryRepository;

    public AdminCategoryServiceImpl(AdminCategoryRepository adminCategoryRepository) {
        this.adminCategoryRepository = adminCategoryRepository;
    }

    @Override
    public CategoryResponseDto addCategory(CategoryDto categoryDto) {
        if (adminCategoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ConflictException("Category with name: " + categoryDto.getName() + " already exist");
        }

        Category category = adminCategoryRepository.save(mapCategoryDtoToCategory(categoryDto));
        return mapCategoryToCategoryResponseDto(category);
    }

    @Override
    public void deleteCategory(int catId) {
        if (!checkIfExist(catId)) {
            throw new NotFoundException("Category with id: " + catId + " doesn't exist");
        }
        adminCategoryRepository.deleteById(catId);
    }

    @Override
    public CategoryResponseDto updateCategory(CategoryDto categoryDto, int catId) {
        if (!checkIfExist(catId)) {
            throw new NotFoundException("Category with id: " + catId + " doesn't exist");
        }

        if (adminCategoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ConflictException("Category with name: " + categoryDto.getName() + " already exist");
        }

        Category category = adminCategoryRepository.save(mapCategoryDtoToCategory(categoryDto));
        return mapCategoryToCategoryResponseDto(category);
    }

    private boolean checkIfExist(int catId) {
        return adminCategoryRepository.existsById(catId);
    }
}
