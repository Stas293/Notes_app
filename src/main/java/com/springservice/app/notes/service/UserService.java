package com.springservice.app.notes.service;

import com.springservice.app.notes.constants.UserConstants;
import com.springservice.app.notes.dto.RoleDto;
import com.springservice.app.notes.dto.UserManagementDto;
import com.springservice.app.notes.models.Role;
import com.springservice.app.notes.models.User;
import com.springservice.app.notes.repository.NoteRepository;
import com.springservice.app.notes.repository.RoleRepository;
import com.springservice.app.notes.repository.UserRepository;
import com.springservice.app.notes.utility.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NoteRepository noteRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.noteRepository = noteRepository;
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(
                Collections.singletonList(
                        roleRepository.findByCode(UserConstants.ROLE_USER).orElseThrow()
                )
        );
        userRepository.save(user);
    }

    public List<UserManagementDto> findAll() {
        return Mapper.mapUsersToUserDtos(userRepository.findAll());
    }

    public UserManagementDto findById(String id) {
        return Mapper.mapUsersToUserDtos(
                List.of(userRepository.findById(id).orElseThrow())
        ).get(0);
    }

    public List<RoleDto> findAllRoles() {
        return Mapper.mapRolesToRoleDtos(roleRepository.findAll());
    }

    public void updateRoles(UserManagementDto user) {
        User userFromDb = userRepository.findById(user.getId()).orElseThrow();
        List<Role> roles = Mapper.mapRoleDtosToRoles(user.getRoles());
        roles.forEach(role -> roleRepository.findByCode(role.getCode()).orElseThrow());
        userFromDb.setRoles(roles);
        userRepository.save(userFromDb);
    }

    public void enableUser(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void disableUser(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void deleteUser(String id) {
        noteRepository.deleteAllByUser_Id(id);
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
