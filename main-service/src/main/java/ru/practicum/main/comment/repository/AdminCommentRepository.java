package ru.practicum.main.comment.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.user.model.User;

import java.util.List;

public interface AdminCommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthor(User user, PageRequest pageRequest);

    List<Comment> findByEventId(Long eventId, PageRequest of);
}
