package com.example.event_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    @GetMapping("/")
    public String helloEvent() {
        return "Hello, Event:)";
    }
}
