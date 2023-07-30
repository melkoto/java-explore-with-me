package ru.practicum.main.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    boolean existsByLatAndLon(Double lat, Double lon);

    Location findByLatAndLon(Double lat, Double lon);
}
