package ru.practicum.main.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.user.dto.CreateUserDto;
import ru.practicum.main.user.dto.UserResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@Validated
public class AdminUserController {

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                          @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get users with ids: {} from: {} to: {} with size of: {}", ids, from, from + size, size);
        return ResponseEntity.status(200).body(null);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        log.info("Create user with name: {} and email: {}", dto.getName(), dto.getEmail());
        return ResponseEntity.status(201).body(null);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") Long userId) {
        log.info("Delete user with id: {}", userId);
        return ResponseEntity.status(204).build();
    }
}
