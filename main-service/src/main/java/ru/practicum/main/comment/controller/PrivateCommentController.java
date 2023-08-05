package ru.practicum.main.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.CreateCommentDto;
import ru.practicum.main.comment.dto.ShortCommentResponseDto;
import ru.practicum.main.comment.service.priv.PrivateCommentService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestControllerAdvice
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final PrivateCommentService service;

    public PrivateCommentController(PrivateCommentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ShortCommentResponseDto> addComment(@Valid CreateCommentDto dto,
                                                              @PathVariable("userId") Long userId,
                                                              @RequestParam Long eventId) {

        log.info("addComment: {}, userId: {}", dto, userId);

        return ResponseEntity.status(201).body(service.addComment(dto, userId, eventId));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ShortCommentResponseDto> updateComment(@Valid CreateCommentDto dto,
                                                                 @PathVariable("commentId") Long commentId,
                                                                 @PathVariable String userId) {
        log.info("updateComment: {}, commentId: {}, userId: {}", dto, commentId, userId);

        return ResponseEntity.status(200).body(service.updateComment(dto, commentId, userId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId,
                                              @PathVariable("userId") Long userId) {

        log.info("deleteComment: {}, userId: {}", commentId, userId);

        service.deleteComment(commentId, userId);

        return ResponseEntity.noContent().build();
    }
}
