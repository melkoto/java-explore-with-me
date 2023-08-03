package ru.practicum.main.user.service.admin;

import ru.practicum.main.user.dto.CreateUserDto;
import ru.practicum.main.user.dto.UserResponseDto;

import java.util.List;

public interface AdminUserService {
    List<UserResponseDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserResponseDto createUser(CreateUserDto dto);

    String deleteUser(Long userId);
}
