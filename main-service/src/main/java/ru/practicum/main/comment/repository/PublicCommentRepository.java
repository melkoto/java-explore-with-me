package ru.practicum.main.comment.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.comment.model.Comment;

import java.util.List;

public interface PublicCommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventId(Long eventId, PageRequest pageRequest);

    Long countByEventId(Long eventId);
}
