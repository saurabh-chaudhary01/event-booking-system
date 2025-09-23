package com.example.notification_service.consumer;

import com.example.kafka_configs.config.TopicLiteral;
import com.example.kafka_configs.event.UserDomainEvent;
import com.example.kafka_configs.event.UserVerifyEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConsumerService {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = TopicLiteral.USER_TOPIC)
    public <T> void userEventListener(UserDomainEvent<T> event) {
        switch (event.getEventType()) {
            case USER_VERIFY -> userVerifyEvent(event);
        }
    }

    private <T> void userVerifyEvent(UserDomainEvent<T> event) {
        UserVerifyEvent payload = objectMapper.convertValue(event.getPayload(), UserVerifyEvent.class);
        log.info("user verify payload {}", payload);
    }
}
