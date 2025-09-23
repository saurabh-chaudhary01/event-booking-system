package com.example.user_service.producer;

import com.example.kafka_configs.config.TopicLiteral;
import com.example.kafka_configs.event.UserDomainEvent;
import com.example.kafka_configs.event.UserEventType;
import com.example.kafka_configs.event.UserVerifyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, Object> template;

    public void userVerifyEvent(UserVerifyEvent payload) {
        UserDomainEvent<UserVerifyEvent> event = new UserDomainEvent<>(UserEventType.USER_VERIFY, payload);
        log.info("payload: {}", payload);
        template.send(TopicLiteral.USER_TOPIC, event);
    }
}
