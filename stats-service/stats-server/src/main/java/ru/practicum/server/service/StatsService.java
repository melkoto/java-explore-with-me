package ru.practicum.server.service;

import ru.practicum.dto.RequestDto;
import ru.practicum.dto.ResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatsService {
    void hit(RequestDto requestDto);

    List<ResponseDto> stats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    Map<Long, Long> getHitsOfUri(List<String> uris);
}
