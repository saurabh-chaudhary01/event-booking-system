package com.example.event_service.service;

import com.example.event_service.dto.EventDTO;
import com.example.event_service.dto.SeatDTO;
import com.example.event_service.entity.EventEntity;
import com.example.event_service.entity.SeatEntity;
import com.example.event_service.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private static EventDTO convertToEventDTO(EventEntity eventEntity) {
        List<SeatDTO> seats = eventEntity.getSeats().stream()
                .map(EventServiceImpl::convertToSeatDTO)
                .toList();

        return EventDTO.builder()
                .id(eventEntity.getId())
                .title(eventEntity.getTitle())
                .description(eventEntity.getDescription())
                .category(eventEntity.getCategory())
                .startTime(eventEntity.getStartTime())
                .endTime(eventEntity.getEndTime())
                .location(eventEntity.getLocation())
                .seats(seats)
                .createdAt(eventEntity.getCreatedAt())
                .updatedAt(eventEntity.getUpdatedAt())
                .build();
    }

    private static SeatDTO convertToSeatDTO(SeatEntity seatEntity) {
        return SeatDTO.builder()
                .id(seatEntity.getId())
                .seatNumber(seatEntity.getSeatNumber())
                .status(seatEntity.getStatus())
                .price(seatEntity.getPrice())
                .build();
    }

    @Override
    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) {
        List<SeatEntity> seats = eventDTO.getSeats().stream()
                .map(seatDTO -> SeatEntity.builder()
                        .seatNumber(seatDTO.getSeatNumber())
                        .status(seatDTO.getStatus())
                        .price(seatDTO.getPrice())
                        .build())
                .toList();

        EventEntity eventEntity = EventEntity.builder()
                .title(eventDTO.getTitle())
                .description(eventDTO.getDescription())
                .category(eventDTO.getCategory())
                .startTime(eventDTO.getStartTime())
                .endTime(eventDTO.getEndTime())
                .location(eventDTO.getLocation())
                .seats(seats)
                .createdAt(eventDTO.getCreatedAt())
                .updatedAt(eventDTO.getUpdatedAt())
                .build();

        EventEntity persistedEvent = eventRepository.save(eventEntity);

        return convertToEventDTO(persistedEvent);
    }

    @Override
    public EventDTO findEventById(long eventId) {
        return eventRepository
                .findById(eventId).map(EventServiceImpl::convertToEventDTO)
                .orElse(null);
    }
}
