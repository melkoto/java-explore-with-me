package ru.practicum.main.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.event.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivateEventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Optional<Event> findByInitiatorIdAndId(Long userId, Long eventId);

    Integer countByTitleAndState(String title, State state);
}
