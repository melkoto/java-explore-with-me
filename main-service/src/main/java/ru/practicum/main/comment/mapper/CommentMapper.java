package ru.practicum.main.comment.mapper;

import ru.practicum.main.comment.dto.CreateCommentDto;
import ru.practicum.main.comment.dto.FullCommentResponseDto;
import ru.practicum.main.comment.dto.ShortCommentResponseDto;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.main.event.mapper.EventMapper.toEventShortDto;
import static ru.practicum.main.user.mapper.UserMapper.userToUserResponseDto;

public class CommentMapper {
    public static Comment mapCreatedToComment(CreateCommentDto createCommentDto, User author, Event event) {
        Comment comment = new Comment();
        comment.setText(createCommentDto.getText());
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());

        return comment;
    }

    public static Comment mapUpdatedToComment(CreateCommentDto createCommentDto, Comment comment) {
        comment.setText(createCommentDto.getText());
        comment.setEditedOn(LocalDateTime.now());

        return comment;
    }

    public static ShortCommentResponseDto mapToShortCommentResponseDto(Comment comment) {
        ShortCommentResponseDto shortCommentResponseDto = new ShortCommentResponseDto();
        shortCommentResponseDto.setId(comment.getId());
        shortCommentResponseDto.setText(comment.getText());
        shortCommentResponseDto.setAuthor(userToUserResponseDto(comment.getAuthor()));
        shortCommentResponseDto.setEvent(toEventShortDto(comment.getEvent()));

        return shortCommentResponseDto;
    }

    public static FullCommentResponseDto mapToFullCommentResponseDto(Comment comment) {
        FullCommentResponseDto fullCommentResponseDto = new FullCommentResponseDto();
        fullCommentResponseDto.setId(comment.getId());
        fullCommentResponseDto.setText(comment.getText());
        fullCommentResponseDto.setAuthor(userToUserResponseDto(comment.getAuthor()));
        fullCommentResponseDto.setEvent(toEventShortDto(comment.getEvent()));
        fullCommentResponseDto.setCreatedOn(comment.getCreatedOn());
        fullCommentResponseDto.setEditedOn(comment.getEditedOn());

        return fullCommentResponseDto;
    }


}
