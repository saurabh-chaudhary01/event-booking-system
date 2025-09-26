package com.example.event_service.controller;

import com.example.event_service.dto.EventRequestDTO;
import com.example.event_service.dto.EventResponseDTO;
import com.example.event_service.dto.SeatResponseDTO;
import com.example.event_service.service.EventService;
import com.example.event_service.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final SeatService seatService;

    @GetMapping("/{eventId}")
    public EventResponseDTO getEventById(@PathVariable("eventId") long eventId) {
        return eventService.findEventById(eventId);
    }

    @GetMapping("/{eventId}/seats")
    public List<SeatResponseDTO> getSeats(@PathVariable("eventId") long eventId) {
        return seatService.getSeatsByEventId(eventId);
    }

    @PostMapping("/")
    public ResponseEntity<EventResponseDTO> createEvent(
            @RequestBody EventRequestDTO eventDTO,
            @RequestHeader("X-User-Role") String userRole
    ) {
        if (!"ADMIN".equals(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        EventResponseDTO response = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
