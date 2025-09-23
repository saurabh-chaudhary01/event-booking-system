package com.example.event_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatResponseDTO {
    private Long id;
    private Integer seatNumber;
    private String status;
    private Double price;
}
