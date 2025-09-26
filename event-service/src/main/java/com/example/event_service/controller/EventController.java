package com.example.event_service.controller;

import com.example.event_service.dto.BookSeatDTO;
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

    @PostMapping("/reserve-seats/{seatId}")
    public ResponseEntity<String> reserveSeat(
            @PathVariable("seatId") long seatId,
            @RequestHeader("X-User-Id") long userId
    ) {
        boolean success = seatService.reserveSeat(seatId, userId);

        if (!success) {
            return ResponseEntity.badRequest().body("seat is booked or currently reserved");
        }

        return ResponseEntity.accepted().body("seat reserved successfully");
    }

    @PostMapping("/book-seats")
    public ResponseEntity<String> bookSeat(
            @RequestBody BookSeatDTO dto,
            @RequestHeader("X-User-Id") long userId) {
        boolean status = seatService.bookSeat(dto.getSeatId(), userId);

        if (!status) {
            return ResponseEntity.badRequest().body("seat is booked or currently reserved");
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("seat booked successfully");
    }
}
