package com.example.spring_heroku.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeHeroku {

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome Heroku";
    }
}
