package ru.practicum.main.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminEventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE (e.initiator.id IN :userIds OR :userIds IS NULL) AND (e.state IN :states OR :states IS NULL) AND (e.category.id IN :categories OR :categories IS NULL) AND (e.eventDate BETWEEN :fromDate AND :toDate)")
    List<Event> getEventsFiltered(
            @Param("userIds") List<Long> users,
            @Param("states") List<String> states,
            @Param("categories") List<Integer> categories,
            @Param("fromDate") LocalDateTime from,
            @Param("toDate") LocalDateTime to,
            PageRequest pr);
}
