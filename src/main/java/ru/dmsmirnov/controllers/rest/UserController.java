package ru.dmsmirnov.controllers.rest;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public String getUserHome(Authentication authentication) {
        return "Hello " + authentication.getName() + "!";
    }

}
