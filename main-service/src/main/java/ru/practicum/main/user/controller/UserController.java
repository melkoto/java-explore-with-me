package ru.practicum.main.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.user.dto.CreateUserDto;
import ru.practicum.main.user.dto.UpdateEventDto;
import ru.practicum.main.user.dto.UpdateEventRequestDto;
import ru.practicum.main.user.dto.UserResponseDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
public class UserController {

    @GetMapping
    public List<UserResponseDto> getEvents(@PathVariable Long userId,
                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Get events for user with id: {}", userId);
        return null;
    }

    @PostMapping
    public List<UserResponseDto> addEvent(@Valid @RequestBody CreateUserDto createUserDto,
                                          @PathVariable Long userId,
                                          @RequestParam(value = "from", defaultValue = "0") Integer from,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Add event for user with id: {}", userId);
        return null;
    }

    @GetMapping("/{eventId}")
    public UserResponseDto getEvent(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) {
        log.info("Get event with id: {} for user with id: {}", eventId, userId);
        return null;
    }

    @PatchMapping("/{eventId}")
    public UserResponseDto updateEvent(@Valid @RequestBody UpdateEventDto updateEventDto,
                                       @PathVariable("userId") Long userId,
                                       @PathVariable("eventId") Long eventId) {
        return null;
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
