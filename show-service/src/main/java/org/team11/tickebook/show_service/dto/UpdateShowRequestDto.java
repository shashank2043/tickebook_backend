package org.team11.tickebook.show_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateShowRequestDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}