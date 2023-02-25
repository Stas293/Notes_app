package com.springservice.app.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NoteDto {
    private String id;
    @Size(min = 3, max = 255, message = "Data must be at least 3 characters and less than 255 characters")
    @NotBlank(message = "Note data is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Note data must be alphanumeric with no special characters")
    private String data;
    private String likes;
    private LocalDate createdAt;
    private UserDto user;
    private boolean canLike;
    private boolean canDislike;
}
