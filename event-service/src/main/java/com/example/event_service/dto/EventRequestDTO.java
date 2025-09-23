package com.example.event_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDTO {
    private String title;
    private String description;
    private String category;
    private Instant startTime;
    private Instant endTime;
    private String location;
    private List<SeatRequestDTO> seats;
    private Instant createdAt;
    private Instant updatedAt;
}
