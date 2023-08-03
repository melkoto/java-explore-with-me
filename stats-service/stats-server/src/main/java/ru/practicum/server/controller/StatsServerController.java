package ru.practicum.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.RequestDto;
import ru.practicum.dto.ResponseDto;
import ru.practicum.server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class StatsServerController {
    private final StatsService statsService;

    @Autowired
    public StatsServerController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public ResponseEntity<Void> hit(@Valid @RequestBody RequestDto requestDto) {
        statsService.hit(requestDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/stats")
    public List<ResponseDto> stats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statsService.stats(start, end, uris, unique);
    }

    @GetMapping("/stats/hits")
    public ResponseEntity<Map<Long, Long>> getViewsOfUri(@RequestParam(name = "uris") List<String> uris) {
        System.out.println(uris);
        return new ResponseEntity<>(statsService.getHitsOfUri(uris), HttpStatus.OK);
    }
}
