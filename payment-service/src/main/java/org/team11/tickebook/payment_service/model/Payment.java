package org.team11.tickebook.payment_service.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    private UUID id;
    private UUID bookingId;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}

