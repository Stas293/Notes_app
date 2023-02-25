package com.springservice.app.notes.utility;

import com.springservice.app.notes.dto.*;
import com.springservice.app.notes.models.Note;
import com.springservice.app.notes.models.Role;
import com.springservice.app.notes.models.User;
import com.springservice.app.notes.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class Mapper {

    private Mapper() {
    }

    public static NoteDto mapNoteToDto(Note note) {
        UserDto userDto = getUserDto(note);
        StringJoiner stringJoiner = getStringLikes(note);
        User currentUser = getCompleteUser();
        return NoteDto.builder()
                .id(note.getId())
                .createdAt(note.getCreatedAt())
                .data(note.getData())
                .user(userDto)
                .likes(stringJoiner.toString())
                .canLike(
                        checkIfUserCanSetLike(note, currentUser)
                )
                .canDislike(
                        checkIfUserCanRemoveLike(note, currentUser)
                )
                .build();
    }

    private static boolean checkIfUserCanRemoveLike(Note note, User currentUser) {
        return note.getLikes()
                .stream()
                .anyMatch(like -> like.getUser()
                        .getUsername()
                        .equals(currentUser.getUsername()));
    }

    private static boolean checkIfUserCanSetLike(Note note, User currentUser) {
        return note.getLikes()
                .stream()
                .noneMatch(like -> like.getUser()
                        .getUsername()
                        .equals(currentUser.getUsername()));
    }

    private static User getCompleteUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String) {
            return User.builder().username("Anonymous").build();
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.user();
    }

    private static StringJoiner getStringLikes(Note note) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        note.getLikes().forEach(
                like ->
                        stringJoiner.add(like.getUser()
                                .getUsername()));
        return stringJoiner;
    }

    private static UserDto getUserDto(Note note) {
        User user = note.getUser();
        return user != null ? UserDto.builder()
                .username(user.getUsername())
                .build() : UserDto.builder().username("Anonymous").build();
    }

    public static User mapRegisterDtoToUser(RegistrationDto registrationDto) {
        return User.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(registrationDto.getPassword())
                .enabled(true)
                .build();
    }

    public static Note newNoteDtoToNote(NewNoteDto newNoteDto) {
        return Note.builder()
                .data(newNoteDto.getData())
                .build();
    }

    public static List<UserManagementDto> mapUsersToUserDtos(List<User> all) {
        return all.stream()
                .map(user -> UserManagementDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .enabled(user.getEnabled())
                        .roles(
                                user.getRoles()
                                        .stream()
                                        .map(role -> new RoleDto(role.getCode()))
                                        .toList()
                        )
                        .build())
                .toList();
    }

    public static List<RoleDto> mapRolesToRoleDtos(List<Role> all) {
        return all.stream()
                .map(role -> new RoleDto(role.getCode()))
                .toList();
    }

    public static List<Role> mapRoleDtosToRoles(List<RoleDto> roles) {
        return roles.stream()
                .map(roleDto -> Role.builder()
                        .code(roleDto.getCode())
                        .build())
                .toList();
    }

    public static Page<NoteDto> toPage(Page<Note> notes) {
        return new PageImpl<>(
                notes.stream()
                        .map(Mapper::mapNoteToDto)
                        .toList(),
                notes.getPageable(),
                notes.getTotalElements()
        );
    }
}
