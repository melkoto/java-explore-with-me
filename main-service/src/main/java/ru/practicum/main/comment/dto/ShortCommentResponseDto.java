package ru.practicum.main.comment.dto;

import lombok.Data;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.user.dto.UserResponseDto;

@Data
public class ShortCommentResponseDto {
    Long id;

    String text;

    ShortEventResponseDto event;

    UserResponseDto author;
}
