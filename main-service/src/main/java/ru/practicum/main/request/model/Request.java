package ru.practicum.main.request.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "requests")
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
