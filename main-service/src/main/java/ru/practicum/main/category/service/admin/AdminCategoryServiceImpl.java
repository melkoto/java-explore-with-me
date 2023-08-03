package ru.practicum.main.category.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.AdminCategoryRepository;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.repository.PublicEventRepository;

import java.util.Optional;

import static ru.practicum.main.category.mapper.CategoryMapper.mapCategoryDtoToCategory;
import static ru.practicum.main.category.mapper.CategoryMapper.mapCategoryToCategoryResponseDto;

@Service
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final AdminCategoryRepository adminCategoryRepository;
    private final PublicEventRepository publicEventRepository;

    public AdminCategoryServiceImpl(AdminCategoryRepository adminCategoryRepository, PublicEventRepository publicEventRepository) {
        this.adminCategoryRepository = adminCategoryRepository;
        this.publicEventRepository = publicEventRepository;
    }

    @Override
    public CategoryResponseDto addCategory(CategoryDto categoryDto) {
        if (adminCategoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ConflictException("Category with name: " + categoryDto.getName() + " already exist");
        }

        Category category = adminCategoryRepository.save(mapCategoryDtoToCategory(categoryDto));

        log.info("Category: {}, with name: {} was added", category, categoryDto.getName());

        return mapCategoryToCategoryResponseDto(category);
    }

    @Override
    public void deleteCategory(int catId) {
        if (checkIfNotExist(catId)) {
            throw new NotFoundException("Category with id: " + catId + " doesn't exist");
        }

        if (!publicEventRepository.findByCategoryId(catId).isEmpty()) {
            throw new ConflictException("Category with id: " + catId + " has events");
        }

        adminCategoryRepository.deleteById(catId);

        log.info("Category with id: {} was deleted", catId);
    }

    @Override
    public CategoryResponseDto updateCategory(CategoryDto categoryDto, int catId) {
        Optional<Category> category = adminCategoryRepository.findByName(categoryDto.getName());

        // TODO tests conflict
        if (category.isPresent()) {
            if (category.get().getName().equals(categoryDto.getName())) {
                log.info("++++++++++++++++++++++++++ {}", categoryDto.getName());
//                throw new ConflictException("Category name: " + categoryDto.getName() + " already exists in the system.");
                return mapCategoryToCategoryResponseDto(category.get());
            } else {
                log.info("------------------ {}", categoryDto.getName());
                throw new ConflictException("Category name: " + categoryDto.getName() + " already exists in the system.");
            }
        }

        Category savedCategory = adminCategoryRepository.save(mapCategoryDtoToCategory(categoryDto));
        log.info("Category: {}, with id: {} was updated", savedCategory, catId);

        return mapCategoryToCategoryResponseDto(savedCategory);
    }

    private boolean checkIfNotExist(int catId) {
        return !adminCategoryRepository.existsById(catId);
    }
}
