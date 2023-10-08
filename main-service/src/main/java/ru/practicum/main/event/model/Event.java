package ru.practicum.main.event.model;

import lombok.Data;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

//TODO Я вижу ты знаешь что такое Lombok. Аннотацию Data нельзя использовать над Entity,
// там было что то с ID точно не помню, но помню что нельзя.
@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", nullable = false, columnDefinition = "TEXT")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column
    private State state;

    @Column(nullable = false, unique = true)
    private String title;

    @Column
    private Long views;

}
