package ru.practicum.main.category.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.category.model.Category;

import java.util.Optional;

@Repository
public interface AdminCategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}
