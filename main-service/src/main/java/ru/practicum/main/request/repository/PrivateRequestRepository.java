package ru.practicum.main.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface PrivateRequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long userId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);

    Long countByEventIdAndStatus(Long eventId, State status);

    @Query("select r " +
            "from Request r " +
            "where r.event.id = ?2 and r.event.initiator.id = ?1"

    )
    List<Request> getUserEventRequests(Long userId, Long eventId);
}
