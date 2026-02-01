package org.team11.tickebook.show_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeat {
    @Id
    @GeneratedValue
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
