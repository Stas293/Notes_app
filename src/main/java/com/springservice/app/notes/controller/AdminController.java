package com.springservice.app.notes.controller;

import com.springservice.app.notes.constants.Pages;
import com.springservice.app.notes.dto.UserManagementDto;
import com.springservice.app.notes.service.NoteService;
import com.springservice.app.notes.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final NoteService noteService;

    public AdminController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return Pages.ADMIN_USERS;
    }

    @DeleteMapping("/notes/{id}")
    public String deleteNote(@PathVariable("id") String id) {
        noteService.deleteNoteById(id);
        return Pages.REDIRECT_NOTES;
    }

    @GetMapping("/users/{id}")
    public String userRoles(@PathVariable("id") String id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", userService.findAllRoles());
        return Pages.ADMIN_MANAGE_USER_ROLES;
    }

    @PatchMapping("/users/enable/{id}")
    public String enableUser(@PathVariable("id") String id) {
        userService.enableUser(id);
        return Pages.REDIRECT_ADMIN;
    }

    @PatchMapping("/users/disable/{id}")
    public String disableUser(@PathVariable("id") String id) {
        userService.disableUser(id);
        return Pages.REDIRECT_ADMIN;
    }

    @PostMapping("/manage-roles")
    public String manageRoles(@ModelAttribute("user") UserManagementDto user) {
        userService.updateRoles(user);
        return Pages.REDIRECT_ADMIN;
    }

    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return Pages.REDIRECT_ADMIN;
    }
}
