package com.example.kafka_configs.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class DomainEvent<E extends Enum<E>, T> {
    private E eventType;
    private T payload;
    private Instant createdAt;

    public DomainEvent(E eventType, T payload) {
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = Instant.now();
    }
}
