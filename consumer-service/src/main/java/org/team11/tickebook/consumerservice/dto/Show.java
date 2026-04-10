package org.team11.tickebook.consumerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Show {
    private UUID id;

    private UUID movieId;   // from movie service
    private Long screenId;  // from theatre service

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Boolean isActive;
}
