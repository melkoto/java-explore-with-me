package ru.practicum.main.comment.model;

import lombok.Data;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 1000, columnDefinition = "TEXT")
    String text;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    Event event;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    User author;

    @Column(nullable = false)
    LocalDateTime createdOn;

    LocalDateTime editedOn;
}
