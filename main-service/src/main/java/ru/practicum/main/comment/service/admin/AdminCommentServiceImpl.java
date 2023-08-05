package ru.practicum.main.comment.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.FullCommentResponseDto;
import ru.practicum.main.comment.repository.AdminCommentRepository;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.util.List;

@Slf4j
@Service
public class AdminCommentServiceImpl implements AdminCommentService {
    private final AdminCommentRepository commentRepository;
    private final AdminUserRepository userRepository;

    public AdminCommentServiceImpl(AdminCommentRepository commentRepository, AdminUserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<FullCommentResponseDto> getUserComments(Long userId, int from, int size) {
        return null;
    }

    @Override
    public void deleteComment(Long commentId) {

    }
}
