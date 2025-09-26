package com.example.kafka_configs.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatBookedEvent {
    private long userId;
    private long eventId;
    private long seatId;
    private double amount;
    private Instant bookedAt;
}
