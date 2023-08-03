package ru.practicum.main.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PublicEventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategoryId(int categoryId);

    @Query("SELECT e FROM Event e WHERE e.state = 'PUBLISHED' AND" +
            "((lower(e.annotation) LIKE lower(concat('%', :text, '%')) OR " +
            "(lower(e.description) LIKE lower(concat('%', :text, '%')))) OR :text IS NULL) AND " +
            "(e.category.id IN :categoriesId OR :categoriesId IS NULL) AND " +
            "(e.paid = :paid OR :paid IS NULL) AND " +
            "(e.eventDate BETWEEN :rangeStart AND :rangeEnd)")
    Page<Event> getEvents(Pageable pageable,
                          @Param("text") String text,
                          @Param("categoriesId") List<Integer> categories,
                          @Param("paid") Boolean paid,
                          @Param("rangeStart") LocalDateTime rangeStart,
                          @Param("rangeEnd") LocalDateTime rangeEnd);

    @Query("SELECT e FROM Event e WHERE e.state = 'PUBLISHED' AND" +
            "((lower(e.annotation) LIKE lower(concat('%', :text, '%')) OR " +
            "(lower(e.description) LIKE lower(concat('%', :text, '%')))) OR :text IS NULL) AND " +
            "(e.category.id IN :categoriesId OR :categoriesId IS NULL) AND " +
            "(e.paid = :paid OR :paid IS NULL) AND " +
            "(e.eventDate BETWEEN :rangeStart AND :rangeEnd) AND " +
            "e.participantLimit > e.confirmedRequests")
    Page<Event> getAvailableEvents(Pageable pageable,
                                   @Param("text") String text,
                                   @Param("categoriesId") List<Integer> categories,
                                   @Param("paid") Boolean paid,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd);
}
