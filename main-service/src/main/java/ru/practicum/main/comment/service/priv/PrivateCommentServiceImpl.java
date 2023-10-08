package ru.practicum.main.comment.service.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.CreateCommentDto;
import ru.practicum.main.comment.dto.ShortCommentResponseDto;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.repository.PrivateCommentRepository;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.PrivateEventRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.AdminUserRepository;

import static ru.practicum.main.comment.mapper.CommentMapper.mapCreatedToComment;
import static ru.practicum.main.comment.mapper.CommentMapper.mapToShortCommentResponseDto;
import static ru.practicum.main.event.eventEnums.State.PUBLISHED;

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
        //TODO Тут тоже не нужно брать сущности из БД
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found."));
        Comment comment = mapCreatedToComment(dto, user, event);

        if (!event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Event with id " + eventId + " is not published");
        }

        Comment savedComment = commentRepository.save(comment);
        ShortCommentResponseDto responseDto = mapToShortCommentResponseDto(savedComment);

        log.info("Saved comment: {}, response after save: {}", savedComment, responseDto);

        return responseDto;
    }

    //TODO Transactional
    @Override
    public ShortCommentResponseDto updateComment(CreateCommentDto dto, Long commentId, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found."));
        Comment comment =
                commentRepository.findById(commentId).orElseThrow(() ->
                        new NotFoundException("Comment with id " + commentId + " not found."));

        if (comment.getAuthor().getId() != user.getId()) {
            throw new ConflictException("User with id " + userId + " is not author of comment with id " + commentId);
        }

        Comment commentEntity = commentRepository.save(mapCreatedToComment(dto, user, comment.getEvent()));
        ShortCommentResponseDto responseDto = mapToShortCommentResponseDto(commentEntity);
        log.info("Updated comment: {}, response after update: {}", commentEntity, responseDto);
        return responseDto;
    }

    //TODO Transactional
    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment =
                commentRepository.findById(commentId).orElseThrow(() ->
                        new NotFoundException("Comment with id " + commentId + " not found."));
        if (comment.getAuthor().getId() != userId) {
            throw new ConflictException("User with id " + userId + " is not author of comment with id " + commentId);
        }

        commentRepository.deleteById(commentId);
        log.info("Comment with id {} deleted", commentId);
    }

}
