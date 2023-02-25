package com.springservice.app.notes.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Document
@AllArgsConstructor
@Builder
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
public class Note implements Serializable {
    @Id
    private String id;
    @Size(min = 3, max = 255, message = "Data must be at least 3 characters and less than 255 characters")
    @NotBlank(message = "Note data is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Note data must be alphanumeric with no special characters")
    private String data;
    private List<Like> likes;
    private LocalDate createdAt;
    @DBRef
    @JsonBackReference
    private User user;

    public void like(Like like) {
        this.likes.add(like);
    }
}
