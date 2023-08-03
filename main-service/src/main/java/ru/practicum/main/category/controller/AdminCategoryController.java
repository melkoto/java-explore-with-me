package ru.practicum.main.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.service.admin.AdminCategoryService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Add category with name: {}", categoryDto.getName());
        return ResponseEntity.status(201).body(adminCategoryService.addCategory(categoryDto));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("catId") int catId) {
        log.info("Delete category with id: {}", catId);
        adminCategoryService.deleteCategory(catId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                                              @PathVariable("catId") int catId) {
        log.info("Update category with id: {}, name: {}", catId, categoryDto.getName());
        return ResponseEntity.status(200).body(adminCategoryService.updateCategory(categoryDto, catId));
    }
}
