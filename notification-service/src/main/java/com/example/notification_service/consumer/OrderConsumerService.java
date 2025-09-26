package com.example.notification_service.consumer;

import com.example.kafka_configs.config.TopicLiteral;
import com.example.kafka_configs.event.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumerService {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = TopicLiteral.ORDER_CREATED_TOPIC)
    public void orderCreatedEvent(OrderCreatedEvent event) {
        log.info("order-created: {}", event);
    }
}
