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
public class OrderCreatedEvent {
    private long userId;
    private long orderId;
    private long eventId;
    private long seatId;
    private double amount;
    private Instant bookedAt;
}
