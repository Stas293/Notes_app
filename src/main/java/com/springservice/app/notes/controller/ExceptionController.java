package com.springservice.app.notes.controller;

import com.springservice.app.notes.exceptions.NotNullableException;
import com.springservice.app.notes.exceptions.UnauthorizedActionException;
import com.springservice.app.notes.models.User;
import com.springservice.app.notes.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(UnauthorizedActionException.class)
    public String handleUnauthorizedActionException(UnauthorizedActionException e) {
        UserDetailsImpl auth = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = auth.user();
        log.warn("Unauthorized action by user: {}", user.getUsername());
        log.warn("Exception: {}", e.toString());
        return "redirect:/";
    }

    @ExceptionHandler(NotNullableException.class)
    public String handleNotNullableException(NotNullableException e) {
        log.warn("Not nullable exception");
        log.warn("Exception: {}", e.toString());
        return "redirect:/";
    }
}
