package ru.practicum.main.compilation.model;

import lombok.Data;
import ru.practicum.main.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "compilations")
public class Compilations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "compilation_event", joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    List<Event> events;

    @Column(name = "pinned")
    private boolean pinned;

    @Column(name = "title", unique = true, nullable = false, length = 50)
    private String title;
}
