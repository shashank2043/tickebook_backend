package org.team11.tickebook.consumerservice.dto;

public record PaymentResponse(
        boolean success,
        String message
) {}
