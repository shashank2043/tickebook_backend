package org.team11.tickebook.payment_service.model;

public record PaymentResponse(
        boolean success,
        String message
) {}
