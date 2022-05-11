package ru.notifier.WebApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class SwaggerController {

    @RequestMapping(value="/docs/", method=GET)
    public String swaggerHtml(){
        return "redirect:/swagger-ui/index.html";
    }
}