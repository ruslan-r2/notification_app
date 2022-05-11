package ru.notifier.WebApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping({"", "/" ,"/list"})
    public String pageUser() {
        return "users";
    }

}