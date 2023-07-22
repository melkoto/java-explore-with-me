package ru.practicum.main.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
@Validated
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryResponseDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Add category with name: {}", categoryDto.getName());
        return categoryService.addCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable("catId") int catId) {
        log.info("Delete category with id: {}", catId);
    }

    @PatchMapping("/{catId}")
    public CategoryResponseDto updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                              @PathVariable("catId") int catId) {
        log.info("Update category with id: {}, name: {}", catId, categoryDto.getName());
        return null;
    }
}
