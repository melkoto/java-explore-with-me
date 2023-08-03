package ru.practicum.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.RequestDto;
import ru.practicum.dto.ResponseDto;
import ru.practicum.dto.ResponseShortDto;
import ru.practicum.server.mapper.StatsMapper;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public void hit(RequestDto requestDto) {
        log.info("Saving hit: {}", requestDto);

        statsRepository.save(StatsMapper.dtoToStats(requestDto));
    }

    @Override
    public List<ResponseDto> stats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Retrieving stats list with parameters: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        return getStatsBasedOnParams(start, end, uris, unique);
    }

    @Override
    public Map<Long, Long> getHitsOfUri(List<String> uris) {
        List<ResponseShortDto> listOfDto = statsRepository.getHitsOfUri(uris);
        return listOfDto.stream()
                .collect(Collectors.toMap(
                        dto -> Long.parseLong(dto.getUri().split("/")[2]), ResponseShortDto::getHits)
                );
    }

    private List<ResponseDto> getStatsBasedOnParams(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (isUriEmpty(uris)) {
            return unique
                    ? statsRepository.getStatsWithUniqueIp(start, end)
                    : statsRepository.getStats(start, end);
        } else {
            return unique
                    ? statsRepository.getStatsWithUniqueIpByUris(start, end, uris)
                    : statsRepository.getStatsByUris(start, end, uris);
        }
    }

    private boolean isUriEmpty(List<String> uris) {
        return uris == null || uris.isEmpty();
    }
}
