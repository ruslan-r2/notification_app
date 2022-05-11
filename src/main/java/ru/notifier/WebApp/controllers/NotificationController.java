package ru.notifier.WebApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @GetMapping({"", "/" ,"/list"})
    public String pageNotification() {
        return "notifications";
    }

}