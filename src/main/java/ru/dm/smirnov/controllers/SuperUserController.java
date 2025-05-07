package ru.dm.smirnov.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SuperUserController {

    @GetMapping("/superuser")
    public String getHello() {
        return "Hello superuser";
    }

}
