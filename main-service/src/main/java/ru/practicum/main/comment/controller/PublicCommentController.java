package ru.practicum.main.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.comment.dto.ShortCommentResponseDto;
import ru.practicum.main.comment.service.pub.PublicCommentService;

import java.util.List;

@Slf4j
@Validated
@RestControllerAdvice
@RequestMapping("/comments")
public class PublicCommentController {
    private final PublicCommentService service;

    public PublicCommentController(PublicCommentService service) {
        this.service = service;
    }

    @GetMapping("/events")
    public ResponseEntity<List<ShortCommentResponseDto>> getEventComments(@RequestParam(value = "eventId") Long eventId,
                                                                          @RequestParam(defaultValue = "0") Integer from,
                                                                          @RequestParam(defaultValue = "10") Integer size) {

        log.info("Get comments for event with id {}", eventId);

        return ResponseEntity.status(200).body(service.getEventComments(eventId, from, size));
    }
}
