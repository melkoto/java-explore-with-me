package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ResponseDto;
import ru.practicum.server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query(value =
            "SELECT s.app, s.uri, COUNT(s.ip) " +
                    "FROM Stats s " +
                    "WHERE s.created BETWEEN ?1 AND ?2 " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY COUNT(s.ip) DESC")
    List<ResponseDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query(value =
            "SELECT s.app, s.uri, COUNT(DISTINCT s.ip) " +
                    "FROM Stats s " +
                    "WHERE s.created BETWEEN ?1 AND ?2 " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ResponseDto> getStatsWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value =
            "SELECT s.app, s.uri, COUNT(DIStiNCT s.ip) " +
                    "FROM Stats s " +
                    "WHERE s.created BETWEEN ?1 AND ?2 " +
                    "AND s.uri IN ?3 " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY COUNT(s.ip) DESC")
    List<ResponseDto> getStatsWithUniqueIpByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value =
            "SELECT s.app, s.uri, COUNT(s.ip) " +
                    "FROM Stats s " +
                    "WHERE s.created BETWEEN ?1 AND ?2 " +
                    "AND s.uri IN ?3 " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY COUNT(s.ip) DESC")
    List<ResponseDto> getStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
