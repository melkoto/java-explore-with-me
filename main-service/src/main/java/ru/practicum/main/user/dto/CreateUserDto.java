package ru.practicum.main.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateUserDto {
    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(min = 6, max = 254, message = "Email should be between 6 and 254 characters")
    private String email;

    @NotEmpty(message = "Name is required")
    @NotBlank(message = "Name should not be blank")
    @Size(min = 2, max = 250, message = "Name should be between 2 and 250 characters")
    private String name;
}
