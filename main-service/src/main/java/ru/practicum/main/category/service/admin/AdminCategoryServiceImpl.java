package ru.practicum.main.category.service.admin;

import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.AdminCategoryRepository;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.repository.EventRepository;

import static ru.practicum.main.category.mapper.CategoryMapper.mapCategoryDtoToCategory;
import static ru.practicum.main.category.mapper.CategoryMapper.mapCategoryToCategoryResponseDto;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final AdminCategoryRepository adminCategoryRepository;
    private final EventRepository eventRepository;

    public AdminCategoryServiceImpl(AdminCategoryRepository adminCategoryRepository, EventRepository eventRepository) {
        this.adminCategoryRepository = adminCategoryRepository;
        this.eventRepository = eventRepository;
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
        if (checkIfNotExist(catId)) {
            throw new NotFoundException("Category with id: " + catId + " doesn't exist");
        }

        if (!eventRepository.findByCategoryId(catId).isEmpty()) {
            throw new ConflictException("Category with id: " + catId + " has events");
        }

        adminCategoryRepository.deleteById(catId);
    }

    @Override
    public CategoryResponseDto updateCategory(CategoryDto categoryDto, int catId) {
        if (checkIfNotExist(catId)) {
            throw new NotFoundException("Category with id: " + catId + " doesn't exist");
        }

        if (adminCategoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ConflictException("Category with name: " + categoryDto.getName() + " already exist");
        }

        Category category = adminCategoryRepository.save(mapCategoryDtoToCategory(categoryDto));
        return mapCategoryToCategoryResponseDto(category);
    }

    private boolean checkIfNotExist(int catId) {
        return !adminCategoryRepository.existsById(catId);
    }
}
