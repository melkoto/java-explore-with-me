package ru.practicum.main.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryResponseDto;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
public class PublicCategoryController {

    @GetMapping
    public List<CategoryResponseDto> getCategories(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Get categories from: {}, size: {}", from, size);
        return null;
    }

    @GetMapping("/{catId}")
    public CategoryResponseDto getCategory(@PathVariable int catId,
                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Get category with id: {}, from: {}, size: {}", catId, from, size);
        return null;
    }
}
