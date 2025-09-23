package com.example.event_service.repository;

import com.example.event_service.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
}
