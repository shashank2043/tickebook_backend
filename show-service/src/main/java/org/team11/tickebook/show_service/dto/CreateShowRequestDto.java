package org.team11.tickebook.show_service.dto;

import lombok.Data;
import org.team11.tickebook.show_service.model.SeatType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class CreateShowRequestDto {
    private UUID movieId;
    private Long screenId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Map<SeatType, BigDecimal> priceMap;
}
