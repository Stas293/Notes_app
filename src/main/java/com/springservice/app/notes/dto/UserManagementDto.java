package com.springservice.app.notes.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserManagementDto {
    private String id;
    private String username;
    private Boolean enabled;
    private List<RoleDto> roles;
}
