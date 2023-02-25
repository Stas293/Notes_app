package com.springservice.app.notes.models;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Builder
public class User implements Serializable {
    @Id
    private String id;
    @Size(min = 3, max = 255, message = "Username must be at least 3 characters and less than 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must be alphanumeric with no spaces")
    @Indexed(unique = true)
    @NotBlank(message = "Username is required")
    private String username;
    @Email(message = "Email should be valid")
    @Indexed(unique = true)
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email must be valid")
    private String email;
    @Size(min = 6, max = 255, message = "Password must be at least 6 characters and less than 255 characters")
    @NotBlank(message = "Password is required")
    private String password;
    private Boolean enabled;
    private List<Note> notes = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
}
