package com.example.event_service.producer;

import com.example.kafka_configs.config.TopicLiteral;
import com.example.kafka_configs.event.SeatBookedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void seatBookedEvent(SeatBookedEvent event) {
        kafkaTemplate.send(TopicLiteral.SEAT_BOOKED_TOPIC, event);
    }
}
