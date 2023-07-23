package ru.practicum.main.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.CreateEventDto;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.dto.UpdateEventUserRequestDto;
import ru.practicum.main.user.dto.UpdateEventDto;
import ru.practicum.main.user.dto.UpdateEventRequestDto;
import ru.practicum.main.user.dto.UserResponseDto;

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
    public UserResponseDto getEventRequest(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) {
        log.info("Get event request with id: {} for user with id: {}", eventId, userId);
        return null;
    }

    @PatchMapping("{eventId}/requests")
    public UserResponseDto updateEventRequest(@Valid @RequestBody UpdateEventRequestDto updateEventRequestDto,
                                              @PathVariable("userId") Long userId,
                                              @PathVariable("eventId") Long eventId) {
        return null;
    }
}
