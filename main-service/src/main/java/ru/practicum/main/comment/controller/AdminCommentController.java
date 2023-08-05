package ru.practicum.main.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.FullCommentResponseDto;
import ru.practicum.main.comment.service.admin.AdminCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestControllerAdvice
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final AdminCommentService service;

    public AdminCommentController(AdminCommentService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FullCommentResponseDto>> getUserComments(
            @PathVariable("userId") Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
            @Positive @RequestParam(defaultValue = "10", required = false) int size) {

        log.info("Get comments for user with id {}", userId);

        return ResponseEntity.status(200).body(service.getUserComments(userId, from, size));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<FullCommentResponseDto>> getEventComments(
            @PathVariable("eventId") Long eventId,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
            @Positive @RequestParam(defaultValue = "10", required = false) int size) {

        log.info("Get comments for event with id {}", eventId);

        return ResponseEntity.status(200).body(null);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {

        log.info("Delete comment with id {}", commentId);

        service.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }
}
