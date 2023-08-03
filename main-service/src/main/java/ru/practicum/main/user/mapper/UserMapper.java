package ru.practicum.main.user.mapper;

import ru.practicum.main.user.dto.CreateUserDto;
import ru.practicum.main.user.dto.UserResponseDto;
import ru.practicum.main.user.model.User;

public class UserMapper {
    public static UserResponseDto userToUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static User userRequestDtoToUser(CreateUserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public static User userResponseDtoToUser(UserResponseDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}
