package ru.practicum.main.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface AdminEventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event e " +
            "where (:users IS NULL or e.initiator.id IN :users) " +
            "and (:states IS NULL or e.state IN :states) " +
            "and (:categories IS NULL or e.category.id IN :categories) " +
            "and (e.eventDate BETWEEN :rangeStart AND :rangeEnd)")
    Page<Event> getEventsByAdmin(@Param("users") Set<Long> users,
                                 @Param("states") List<State> states,
                                 @Param("categories") List<Integer> categories,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

    Set<Event> findAllByIdIn(Set<Long> eventIds);

    Set<Event> findByIdInAndState(Set<Long> eventIds, State state);
}
