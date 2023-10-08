package ru.practicum.main.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "compilation_event", joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    Set<Event> events;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //TODO рассположи поле ИД сверху, ни разу не видел его ниже остальных. Соблюдай разрывы между полями, плохо читается.
    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title", unique = true, nullable = false, length = 50)
    private String title;
}
