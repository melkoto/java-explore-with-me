package ru.practicum.dto;

import lombok.Data;

@Data
public class ResponseDto {
    String app;
    String uri;
    Long hits;
}
