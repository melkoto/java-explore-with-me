package ru.practicum.main.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.CreateEventDto;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.dto.UpdateEventUserRequestDto;
import ru.practicum.main.event.service.priv.PrivateEventService;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResponseDto;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestControllerAdvice
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    public PrivateEventController(PrivateEventService privateEventService) {
        this.privateEventService = privateEventService;
    }

    @GetMapping
    public ResponseEntity<List<ShortEventResponseDto>> getEvents(@PathVariable Long userId,
                                                                 @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("\nGet events for user with id: {}," +
                "\nfrom: {}," +
                "\nsize: {}", userId, from, size);
        return ResponseEntity.status(200).body(privateEventService.getEvents(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<FullEventResponseDto> addEvent(@Valid @RequestBody CreateEventDto createEventDto,
                                                         @PathVariable Long userId) {
        log.info("\nAdd event for user with body: {}" +
                "\nand user with id: {}", createEventDto, userId);
        return ResponseEntity.status(201).body(privateEventService.addEvent(createEventDto, userId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<FullEventResponseDto> getEvent(@PathVariable("userId") Long userId,
                                                         @PathVariable("eventId") Long eventId) {
        log.info("\nGet event with id: {}" +
                "\nfor user with id: {}", eventId, userId);
        return ResponseEntity.status(200).body(privateEventService.getEvent(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<FullEventResponseDto> updateEvent(@Valid @RequestBody UpdateEventUserRequestDto updateEventUserRequestDto,
                                                            @PathVariable("userId") Long userId,
                                                            @PathVariable("eventId") Long eventId,
                                                            HttpServletRequest request) {
        log.info("\nUpdate event with id: {}" +
                        "\nwith user id: {}" +
                        "\nwith body: \n{}," +
                        "\nuri: {}",
                eventId, userId, updateEventUserRequestDto, request.getRequestURI());
        return ResponseEntity.status(200).body(privateEventService.updateEvent(updateEventUserRequestDto, userId, eventId));
    }

    @GetMapping("{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventRequests(@PathVariable("userId") Long userId,
                                                                          @PathVariable("eventId") Long eventId) {
        log.info("\nGet event request with id: {}" +
                "\nfor user with id: {}", eventId, userId);
        return ResponseEntity.status(200).body(privateEventService.getEventRequests(userId, eventId));
    }

    @PatchMapping("{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResponseDto> updateEventRequest(@Valid @RequestBody EventRequestStatusUpdateRequestDto dto,
                                                                                  @PathVariable("userId") Long userId,
                                                                                  @PathVariable("eventId") Long eventId,
                                                                                  HttpServletRequest request) {
        log.info("\nUpdate event request with id: {}" +
                "\nfor user with id: {}" +
                "\nwith body: {}" +
                "\nuri: {}", eventId, userId, dto, request.getRequestURI());
        return ResponseEntity.status(200).body(privateEventService.updateEventRequest(dto, userId, eventId));
    }
}
