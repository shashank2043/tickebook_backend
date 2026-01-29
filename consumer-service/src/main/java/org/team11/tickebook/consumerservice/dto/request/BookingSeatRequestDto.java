package org.team11.tickebook.consumerservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatRequestDto {

    private UUID bookingId;
    private UUID seatId;
    private UUID showId;
    private BigDecimal seatPrice;
}
