package org.team11.tickebook.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team11.tickebook.auth_service.model.entity.Otp;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Long> {

    Optional<Otp> findTopByEmailOrderByExpiresAtDesc(String email);
}
