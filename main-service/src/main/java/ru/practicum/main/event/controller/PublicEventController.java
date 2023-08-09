package ru.practicum.main.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.eventEnums.SortTypes;
import ru.practicum.main.event.service.pub.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.utils.Constants.DATE_TIME_FORMAT;

@RestControllerAdvice
@RequestMapping("events")
@Slf4j
@Validated
public class PublicEventController {
    private final PublicEventService publicEventService;

    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping
    public ResponseEntity<List<ShortEventResponseDto>> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(name = "categories", required = false) List<Integer> categoriesId,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false, defaultValue = "1970-01-01 00:00:00")
            @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false, defaultValue = "3970-01-01 00:00:00")
            @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") SortTypes sortType,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
            @Positive @RequestParam(defaultValue = "10", required = false) int size, HttpServletRequest request) {

        log.info("Get events with \ntext: {}, \ncategories: {}, \npaid: {}, \nrangeStart: {}, \nrangeEnd: {}, " +
                        "\nonlyAvailable: {}, \nsort: {}, \nfrom: {}, \nsize: {}",
                text, categoriesId, paid, rangeStart, rangeEnd,
                onlyAvailable, sortType, from, size);

        return ResponseEntity.status(200).body(publicEventService.getEvents(text, categoriesId, paid, rangeStart,
                rangeEnd, onlyAvailable, sortType, from, size, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullEventResponseDto> getEvent(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("Get event with id: {}, request: {}", id, request);
        return ResponseEntity.status(200).body(publicEventService.getEvent(id.intValue(), request));
    }
}
