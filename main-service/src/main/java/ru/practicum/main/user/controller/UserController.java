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


}
