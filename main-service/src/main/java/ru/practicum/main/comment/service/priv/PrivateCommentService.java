package ru.practicum.main.comment.service.priv;

import ru.practicum.main.comment.dto.CreateCommentDto;
import ru.practicum.main.comment.dto.ShortCommentResponseDto;

public interface PrivateCommentService {
    ShortCommentResponseDto addComment(CreateCommentDto dto, Long userId, Long eventId);

    ShortCommentResponseDto updateComment(CreateCommentDto dto, Long commentId, String userId);

    void deleteComment(Long commentId, Long userId);
}
