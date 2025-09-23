package com.example.event_service.service;

import com.example.event_service.dto.EventDTO;

public interface EventService {
    EventDTO createEvent(EventDTO eventDTO);

    EventDTO findEventById(long eventId);
}
