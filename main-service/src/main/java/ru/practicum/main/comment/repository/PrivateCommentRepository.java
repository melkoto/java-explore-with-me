package ru.practicum.main.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.comment.model.Comment;

public interface PrivateCommentRepository extends JpaRepository<Comment, Long> {
}
