package com.example.event_service.service;

import com.example.event_service.dto.EventRequestDTO;
import com.example.event_service.dto.EventResponseDTO;

public interface EventService {
    EventResponseDTO createEvent(EventRequestDTO eventDTO);

    EventResponseDTO findEventById(long eventId);
}
