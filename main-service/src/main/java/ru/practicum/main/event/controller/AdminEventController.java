package ru.practicum.main.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.UpdateEventDto;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.event.service.admin.AdminEventService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@Slf4j
@Validated
@RestControllerAdvice
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;

    public AdminEventController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    @GetMapping
    public ResponseEntity<List<FullEventResponseDto>> getEvents(
            @RequestParam(name = "users", required = false) Set<Long> users,
            @RequestParam(name = "states", required = false) List<State> states,
            @RequestParam(name = "categories", required = false) List<Integer> categories,
            @RequestParam(name = "rangeStart", required = false, defaultValue = "1970-01-01 00:00:00")
            @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false, defaultValue = "3100-01-01 00:00:00")
            @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {

        log.info("Get events with \nusers: {}, \nstates: {}, \ncategories: {}, \nrangeStart: {}, \nrangeEnd: {}, " +
                "\nfrom: {}, \nsize: {}.", users, states, categories, rangeStart, rangeEnd, from, size);

        return ResponseEntity
                .status(200)
                .body(adminEventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<FullEventResponseDto> updateEvent(@PathVariable @NotNull Long eventId,
                                                            @RequestBody @Valid UpdateEventDto event) {
        log.info("Update event with \nid: {} and \ndata: {}", eventId, event);
        return ResponseEntity.status(200).body(adminEventService.updateEvent(eventId, event));
    }
}
