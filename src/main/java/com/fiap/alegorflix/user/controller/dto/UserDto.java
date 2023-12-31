package com.fiap.alegorflix.user.controller.dto;

import com.fiap.alegorflix.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(title = "UserDto", description = "Object that represents a User's data transfer object")
public record UserDto(
    @NotBlank(message = "fullName is mandatory")
    @Size(min = 2, max = 50, message = "size must be between {min} and {max}")
    @Schema(description = "fullName to identify the user", example = "José Fulano")
    String name,

    @NotBlank(message = "email is mandatory")
    @Schema(description = "email to register the user", example = "jose@example.com")
    @Email
    String email,
    
    @NotBlank(message = "password is mandatory")
    @Size(min = 6, max = 15, message = "must have {min} and {max} characters")
    @Schema(description = "password to log in to the system", example = "strongPass123!")
    String password) {

    public User getUserUpdated(User oldUser) {
        oldUser.setName(name);
        oldUser.setEmail(email);
        oldUser.setPassword(password);
        return oldUser;
    }

}
