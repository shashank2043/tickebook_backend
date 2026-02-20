package org.team11.tickebook.payment_service.model;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        UUID bookingId,
        BigDecimal amount
) {}
