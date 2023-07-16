package ru.practicum.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.RequestDto;
import ru.practicum.dto.ResponseDto;
import ru.practicum.server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class StatsServerController {
    private final StatsService statsService;

    @Autowired
    public StatsServerController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public void hit(@Valid @RequestBody RequestDto requestDto) {
        statsService.hit(requestDto);
    }

    @GetMapping("/stats")
    public List<ResponseDto> stats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statsService.stats(start, end, uris, unique);
    }
}
