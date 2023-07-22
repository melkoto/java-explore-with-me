package ru.practicum.main.event.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lat")
    private float lat;

    @Column(name = "lon")
    private float lon;
}
