package ru.practicum.main.event.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
