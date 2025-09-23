package com.example.kafka_configs.event;

public class UserDomainEvent<T> extends DomainEvent<UserEventType, T> {
    public UserDomainEvent() {
    }

    public UserDomainEvent(UserEventType eventType, T payload) {
        super(eventType, payload);
    }
}
