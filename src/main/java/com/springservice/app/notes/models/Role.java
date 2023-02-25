package com.springservice.app.notes.models;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Data
@Document
@AllArgsConstructor
@Builder
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
public class Role implements GrantedAuthority {
    @Id
    private String id;
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Role name must be alphanumeric with no special characters")
    private String name;
    @Pattern(regexp = "^[A-Z0-9 _]+$", message = "Role code must be alphanumeric with no special characters")
    private String code;

    @Override
    public String getAuthority() {
        return this.code;
    }
}
