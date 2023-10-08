package ru.practicum.main.comment.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.FullCommentResponseDto;
import ru.practicum.main.comment.mapper.CommentMapper;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.repository.AdminCommentRepository;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.util.List;
import java.util.stream.Collectors;

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
        //TODO Это не есть хорошо, получать целого юзера только для того что бы получить другую сущность.
        // Ты можешь сделать HQL запрос через аннотацию Query в репозитории
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found."));

        List<Comment> comments = commentRepository.findByAuthor(user, PageRequest.of(from, size));

        log.info("Found {} comments for user with id {}", comments.size(), userId);

        return comments
                .stream()
                .map(CommentMapper::mapToFullCommentResponseDto)
                .collect(Collectors.toList());
    }

    //TODO Transactional
    @Override
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment with id " + commentId + " not found.");
        }

        commentRepository.deleteById(commentId);
        log.info("Comment with id {} deleted", commentId);
    }

    @Override
    public List<FullCommentResponseDto> getEventComments(Long eventId, int from, int size) {
        List<Comment> comments = commentRepository.findByEventId(eventId, PageRequest.of(from, size));

        log.info("Found {} comments for event with id {}", comments.size(), eventId);

        return comments
                .stream()
                .map(CommentMapper::mapToFullCommentResponseDto)
                .collect(Collectors.toList());
    }
}
