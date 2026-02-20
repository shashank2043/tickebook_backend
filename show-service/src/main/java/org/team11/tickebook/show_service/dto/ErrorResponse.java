package org.team11.tickebook.show_service.dto;

public record ErrorResponse(
        String code,
        String message
) {}