package org.team11.tickebook.consumerservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShowSeat {
    private UUID id;

    private UUID showId;
    private Long seatId; // from theatre service

    @Enumerated(EnumType.STRING)
    private ShowSeatStatus status;

    private LocalDateTime lockedAt;
    private UUID lockedByUserId;

    private SeatType seatType;
    private BigDecimal price;
}
