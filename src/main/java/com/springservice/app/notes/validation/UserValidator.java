package com.springservice.app.notes.validation;

import com.springservice.app.notes.models.User;
import com.springservice.app.notes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "login.taken", "Username is already taken");
        }

        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "email.taken", "Email is already taken");
        }
    }
}
