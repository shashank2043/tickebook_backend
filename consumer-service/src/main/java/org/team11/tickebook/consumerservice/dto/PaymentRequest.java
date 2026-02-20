package org.team11.tickebook.consumerservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        UUID bookingId,
        BigDecimal amount
) {}
