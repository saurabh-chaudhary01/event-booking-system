package com.example.user_service.repository;

import com.example.user_service.entity.UserVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerificationEntity, Long> {
    Optional<UserVerificationEntity> findByUserId(long userId);
}
