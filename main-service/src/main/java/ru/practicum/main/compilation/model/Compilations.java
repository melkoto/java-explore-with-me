package ru.practicum.main.compilation.model;

import lombok.Data;
import ru.practicum.main.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "compilations")
public class Compilations {
    @ManyToMany
    @JoinTable(name = "compilation_event", joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    List<Event> events;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pinned")
    private boolean pinned;
    @Column(name = "title")
    private String title;
}
