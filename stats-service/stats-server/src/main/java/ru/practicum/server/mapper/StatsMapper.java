package ru.practicum.server.mapper;

import ru.practicum.dto.RequestDto;
import ru.practicum.server.model.Stats;

import java.time.LocalDateTime;

public class StatsMapper {
    public static Stats dtoToStats(RequestDto requestDto) {
        Stats stats = new Stats();
        stats.setApp(requestDto.getApp());
        stats.setUri(requestDto.getUri());
        stats.setIp(requestDto.getIp());
        stats.setCreated(LocalDateTime.parse(requestDto.getTime()));
        return stats;
    }
}
