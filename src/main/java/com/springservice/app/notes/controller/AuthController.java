package com.springservice.app.notes.controller;

import com.springservice.app.notes.constants.Pages;
import com.springservice.app.notes.dto.RegistrationDto;
import com.springservice.app.notes.models.User;
import com.springservice.app.notes.service.UserService;
import com.springservice.app.notes.utility.Mapper;
import com.springservice.app.notes.validation.UserValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final UserValidator userValidator;

    public AuthController(UserService userService,
                          UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String login() {
        return Pages.AUTH_LOGIN;
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("user") RegistrationDto user) {
        return Pages.AUTH_REGISTER;
    }

    @PostMapping("/register")
    public String registerProcess(@Valid @ModelAttribute("user") RegistrationDto registrationDto,
                                  BindingResult bindingResult) {
        User user = Mapper.mapRegisterDtoToUser(registrationDto);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return Pages.AUTH_REGISTER;
        }
        userService.register(user);
        return Pages.REDIRECT_AUTH_LOGIN;
    }
}
