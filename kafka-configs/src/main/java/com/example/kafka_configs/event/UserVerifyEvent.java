package com.example.kafka_configs.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVerifyEvent {
    private long userId;
    private String name;
    private String email;
    private String token;
}