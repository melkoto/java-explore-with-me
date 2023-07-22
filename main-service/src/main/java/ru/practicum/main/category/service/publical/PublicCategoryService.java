package ru.practicum.main.category.service.publical;

import org.springframework.stereotype.Repository;
import ru.practicum.main.category.dto.CategoryResponseDto;

import java.util.List;

@Repository
public interface PublicCategoryService {
    CategoryResponseDto getCategory(int catId);

    List<CategoryResponseDto> getCategories(Integer from, Integer size);
}
