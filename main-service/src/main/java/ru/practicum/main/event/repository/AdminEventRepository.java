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

    @Query("SELECT e FROM Event e WHERE " +
            "(e.initiator.id IN :userIds OR :userIds IS NULL) AND " +
            "(e.state IN :states OR :states IS NULL) AND " +
            "(e.category.id IN :categories OR :categories IS NULL) AND " +
            "(e.eventDate BETWEEN :fromDate AND :toDate)")
    Page<Event> getFilteredEvents(@Param("userIds") Set<Long> userIds,
                                  @Param("states") List<State> states,
                                  @Param("categories") List<Integer> categories,
                                  @Param("fromDate") LocalDateTime fromDate,
                                  @Param("toDate") LocalDateTime toDate,
                                  Pageable pageable);

    Set<Event> findAllByIdIn(Set<Long> eventIds);

    Set<Event> findByIdInAndState(Set<Long> eventIds, State state);
}
