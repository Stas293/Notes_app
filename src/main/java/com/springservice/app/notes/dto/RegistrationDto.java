package com.springservice.app.notes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationDto {
    @Size(min = 3, max = 255, message = "Username must be at least 3 characters and less than 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must be alphanumeric with no spaces")
    @NotBlank(message = "Username is required")
    private String username;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email must be valid")
    private String email;
    @Size(min = 6, max = 255, message = "Password must be at least 6 characters and less than 255 characters")
    @NotBlank(message = "Password is required")
    private String password;
}
