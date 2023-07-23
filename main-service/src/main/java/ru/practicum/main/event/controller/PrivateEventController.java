package ru.practicum.main.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.CreateEventDto;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.dto.UpdateEventUserRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResponseDto;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
public class PrivateEventController {
    @GetMapping
    public ResponseEntity<List<ShortEventResponseDto>> getEvents(@PathVariable Long userId,
                                                                 @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Get events for user with id: {}, from: {}, size: {}", userId, from, size);
        return ResponseEntity.status(200).body(null);
    }

    @PostMapping
    public ResponseEntity<List<FullEventResponseDto>> addEvent(@Valid @RequestBody CreateEventDto createEventDto,
                                                               @PathVariable Long userId,
                                                               @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Add event for user with body: {}, id: {}, from: {}, size: {}", createEventDto, userId, from, size);
        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<FullEventResponseDto> getEvent(@PathVariable("userId") Long userId,
                                                         @PathVariable("eventId") Long eventId) {
        log.info("Get event with id: {} for user with id: {}", eventId, userId);
        return ResponseEntity.status(200).body(null);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<FullEventResponseDto> updateEvent(@Valid @RequestBody UpdateEventUserRequestDto updateEventUserRequestDto,
                                                            @PathVariable("userId") Long userId,
                                                            @PathVariable("eventId") Long eventId) {
        log.info("Update event with id: {} for user with id: {} with body: {}", eventId, userId, updateEventUserRequestDto);
        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("{eventId}/requests")
    public ResponseEntity<ParticipationRequestDto> getEventRequest(@PathVariable("userId") Long userId,
                                                                   @PathVariable("eventId") Long eventId) {
        log.info("Get event request with id: {} for user with id: {}", eventId, userId);
        return ResponseEntity.status(200).body(null);
    }

    @PatchMapping("{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResponseDto> updateEventRequest(@Valid @RequestBody EventRequestStatusUpdateRequestDto dto,
                                                                                  @PathVariable("userId") Long userId,
                                                                                  @PathVariable("eventId") Long eventId) {
        log.info("Update event request with id: {} for user with id: {} with body: {}", eventId, userId, dto);
        return ResponseEntity.status(200).body(null);
    }
}
