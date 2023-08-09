package ru.practicum.main.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryResponseDto;
import ru.practicum.main.category.service.pub.PublicCategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
public class PublicCategoryController {

    private final PublicCategoryService publicCategoryService;

    public PublicCategoryController(PublicCategoryService publicCategoryService) {
        this.publicCategoryService = publicCategoryService;
    }


    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories(
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        log.info("Get categories from: {}, size: {}", from, size);

        return ResponseEntity.status(200).body(publicCategoryService.getCategories(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable int catId) {
        log.info("Get category with id: {}", catId);
        return ResponseEntity.status(200).body(publicCategoryService.getCategory(catId));
    }
}
