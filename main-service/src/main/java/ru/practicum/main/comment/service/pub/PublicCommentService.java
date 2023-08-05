package ru.practicum.main.comment.service.pub;

import ru.practicum.main.comment.dto.ShortCommentResponseDto;

import java.util.List;

public interface PublicCommentService {
    List<ShortCommentResponseDto> getEventComments(Long eventId, Integer from, Integer size);
}
