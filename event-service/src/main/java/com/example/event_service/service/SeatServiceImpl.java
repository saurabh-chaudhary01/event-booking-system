package com.example.event_service.service;

import com.example.event_service.dto.SeatResponseDTO;
import com.example.event_service.entity.SeatEntity;
import com.example.event_service.producer.OrderProducerService;
import com.example.event_service.repository.SeatRepository;
import com.example.kafka_configs.event.SeatBookedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private static final String RESERVATION_KEY_PREFIX = "reservation:";

    private final StringRedisTemplate redisTemplate;
    private final SeatRepository seatRepository;
    private final OrderProducerService orderProducerService;

    private static SeatResponseDTO convertToSeatDTO(SeatEntity seatEntity) {
        return SeatResponseDTO.builder()
                .id(seatEntity.getId())
                .seatNumber(seatEntity.getSeatNumber())
                .status(seatEntity.getStatus())
                .price(seatEntity.getPrice())
                .build();
    }

    @Override
    public List<SeatResponseDTO> getSeatsByEventId(long eventId) {
        return seatRepository.findByEventId(eventId)
                .stream()
                .map(seatEntity -> {
                    SeatResponseDTO seatDTO = convertToSeatDTO(seatEntity);
                    if (isSeatReserved(seatDTO.getId())) seatDTO.setStatus("RESERVED");
                    return seatDTO;
                })
                .toList();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean reserveSeat(long seatId, long userId) {
        SeatEntity seat = seatRepository.findById(seatId).orElse(null);
        if (seat == null || Objects.equals(seat.getStatus(), "BOOKED")) {
            return false;
        }

        String key = RESERVATION_KEY_PREFIX + seatId;

        // SETNX with TTL (reserve only if not already reserved)
        Boolean success = redisTemplate.opsForValue().setIfAbsent(
                key,
                String.valueOf(userId),
                Duration.ofMinutes(10)
        );

        if (Boolean.TRUE.equals(success)) {
            log.info("Seat {} reserved for user {} for 10 minutes", seatId, userId);
            return true;
        }

        return false; // seat already reserved
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean bookSeat(long seatId, long userId) {
        SeatEntity seat = seatRepository.findById(seatId).orElse(null);
        if (seat == null || Objects.equals(seat.getStatus(), "BOOKED")) {
            return false;
        }

        String key = RESERVATION_KEY_PREFIX + seatId;
        String reservedUser = redisTemplate.opsForValue().get(key);

        // Must either be reserved by this user OR no reservation exists
        if (reservedUser != null && !reservedUser.equals(String.valueOf(userId))) {
            return false; // reserved by another user
        }

        seat.setStatus("BOOKED");
        seatRepository.save(seat);

        redisTemplate.delete(key); // clear reservation

        log.info("Seat {} booked permanently by user {}", seatId, userId);

        // send event to order services
        SeatBookedEvent event = SeatBookedEvent.builder()
                .seatId(seatId)
                .userId(userId)
                .amount(seat.getPrice())
                .bookedAt(Instant.now())
                .build();

        orderProducerService.seatBookedEvent(event);

        return true;
    }

    @Override
    public boolean isSeatReserved(long seatId) {
        String key = RESERVATION_KEY_PREFIX + seatId;
        return redisTemplate.hasKey(key);
    }
}
