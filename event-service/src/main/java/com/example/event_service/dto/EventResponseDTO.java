package com.example.event_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Instant startTime;
    private Instant endTime;
    private String location;
    private Instant createdAt;
    private Instant updatedAt;
}
