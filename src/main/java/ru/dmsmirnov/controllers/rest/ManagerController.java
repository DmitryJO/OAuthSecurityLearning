package ru.dmsmirnov.controllers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerController {

    @GetMapping("/manager")
    public String getHello() {
        return "Hello manager!";
    }

}
