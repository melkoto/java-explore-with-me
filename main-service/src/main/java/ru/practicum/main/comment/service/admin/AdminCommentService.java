package ru.practicum.main.comment.service.admin;

import ru.practicum.main.comment.dto.FullCommentResponseDto;

import java.util.List;

public interface AdminCommentService {
    List<FullCommentResponseDto> getUserComments(Long userId, int from, int size);

    void deleteComment(Long commentId);

    List<FullCommentResponseDto> getEventComments(Long eventId, int from, int size);
}
