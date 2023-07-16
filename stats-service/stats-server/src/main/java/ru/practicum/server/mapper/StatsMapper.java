package ru.practicum.server.mapper;

import ru.practicum.dto.RequestDto;
import ru.practicum.dto.ResponseDto;
import ru.practicum.server.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {
    public static Stats dtoToStats(RequestDto requestDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Stats stats = new Stats();
        stats.setApp(requestDto.getApp());
        stats.setUri(requestDto.getUri());
        stats.setIp(requestDto.getIp());
        stats.setCreated(LocalDateTime.parse(requestDto.getTime(), formatter));
        return stats;
    }

    public static ResponseDto statsToDto(Stats stats) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setApp(stats.getApp());
        responseDto.setUri(stats.getUri());
        return responseDto;
    }
}
