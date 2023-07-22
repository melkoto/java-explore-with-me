package ru.practicum.main.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.event.dto.ShortEventResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("events")
@Slf4j
@Validated
public class PublicEventController {

    @GetMapping
    public ResponseEntity<List<ShortEventResponseDto>> getEvents(@RequestParam(required = false) String text,
                                                                 @RequestParam(name = "categories", required = false) List<Long> categoriesId,
                                                                 @RequestParam(required = false) Boolean paid,
                                                                 @RequestParam(required = false) String rangeStart,
                                                                 @RequestParam(required = false) String rangeEnd,
                                                                 @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                                 @RequestParam(required = false) String sort,
                                                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                                 @Positive @RequestParam(defaultValue = "10") int size,
                                                                 HttpServletRequest request) {
        log.info("Get events with \ntext: {}, \ncategories: {}, \npaid: {}, \nrangeStart: {}, \nrangeEnd: {}, " +
                "\nonlyAvailable: {}, \nsort: {}, \nfrom: {}, \nsize: {}", text, categoriesId, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        return ResponseEntity.status(200).body(null);
    }
}
