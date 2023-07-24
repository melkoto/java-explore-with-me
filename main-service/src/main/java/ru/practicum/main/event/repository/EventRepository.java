package ru.practicum.main.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategoryId(int categoryId);

    List<Event> findByInitiatorId(long userId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.state = 'PUBLISHED' " +
            "AND LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND e.category IN :categories " +
            "AND e.paid = :paid " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND e.eventDate > CURRENT_DATE " +
            "AND e.confirmedRequests < e.participantLimit")
    Page<Event> findAllEvents(@Param("text") String text,
                              @Param("categories") List<Integer> categories,
                              @Param("paid") Boolean paid,
                              @Param("rangeStart") LocalDateTime rangeStart,
                              @Param("rangeEnd") LocalDateTime rangeEnd,
                              Pageable pageable);
}
