package com.example.notification_service.consumer;

import com.example.kafka_configs.config.App_Constant;
import com.example.kafka_configs.event.UserVerifyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserConsumerService {

    @KafkaListener(topics = App_Constant.USER_VERIFY_TOPIC)
    public void userVerifyEvent(UserVerifyEvent event) {
        log.info("UserCreatedEvent {}", event);
    }
}
