package ru.practicum.main.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateCommentDto {
    @NotNull
    @NotBlank
    @Size(min = 5, max = 1000, message = "Comment text must be between 5 and 1000 characters")
    String text;
}
