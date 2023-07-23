package ru.practicum.main.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByCategoryId(int categoryId);
}
