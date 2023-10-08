package ru.practicum.main.comment.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.user.model.User;

import java.util.List;

public interface AdminCommentRepository extends JpaRepository<Comment, Long> {

    //TODO

//     @Query("SELECT Comment FROM Comment c WHERE c.author.id = :id")
//     List<Comment> findByAuthor(@Param("id") Long id, PageRequest pageRequest);

//    Я не проверял, но должно сработать. В любом случае, надеюсь, суть ты уловил

    List<Comment> findByAuthor(User user, PageRequest pageRequest);

    List<Comment> findByEventId(Long eventId, PageRequest of);
}
