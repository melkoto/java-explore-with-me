package ru.practicum.main.comment.service.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.CreateCommentDto;
import ru.practicum.main.comment.dto.ShortCommentResponseDto;
import ru.practicum.main.comment.repository.PrivateCommentRepository;
import ru.practicum.main.event.repository.PrivateEventRepository;
import ru.practicum.main.user.repository.AdminUserRepository;

@Slf4j
@Service
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final PrivateCommentRepository commentRepository;
    private final AdminUserRepository userRepository;
    private final PrivateEventRepository eventRepository;

    public PrivateCommentServiceImpl(PrivateCommentRepository commentRepository, AdminUserRepository userRepository, PrivateEventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public ShortCommentResponseDto addComment(CreateCommentDto dto, Long userId, Long eventId) {
        return null;
    }

    @Override
    public ShortCommentResponseDto updateComment(CreateCommentDto dto, Long commentId, String userId) {
        return null;
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {

    }
}
