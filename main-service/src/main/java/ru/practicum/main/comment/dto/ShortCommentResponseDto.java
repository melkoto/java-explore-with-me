package ru.practicum.main.comment.dto;

import lombok.Data;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.user.dto.UserResponseDto;

import java.time.LocalDateTime;

@Data
public class ShortCommentResponseDto {
    String text;

    ShortEventResponseDto event;

    UserResponseDto author;

    LocalDateTime createdOn;

    LocalDateTime editedOn;
}
