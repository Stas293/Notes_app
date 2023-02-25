package com.springservice.app.notes.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForwardController {
    @GetMapping(value = {"/about", "/error"})
    public String forward(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.substring(1);
    }
}
