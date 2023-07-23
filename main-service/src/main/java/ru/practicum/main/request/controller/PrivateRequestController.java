package ru.practicum.main.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
public class PrivateRequestController {

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable(name = "userId") Long userId) {
        log.info("Get requests for user with id {}", userId);
        return ResponseEntity.status(200).body(null);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable(name = "userId") Long userId,
                                                                 @RequestParam(name = "eventId") Long eventId) {
        log.info("Create request for user with id {} and event with id {}", userId, eventId);
        return ResponseEntity.status(201).body(null);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable(name = "userId") Long userId,
                                                                 @PathVariable(name = "requestId") Long requestId) {
        log.info("Cancel request with id {} for user with id {}", requestId, userId);
        return ResponseEntity.status(200).body(null);
    }
}
