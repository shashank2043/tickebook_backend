package org.team11.tickebook.consumerservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.consumerservice.model.BookingSeatStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatResponseDto {

    private UUID id;
    private UUID bookingId;
    private UUID seatId;
    private UUID showId;
    private BigDecimal seatPrice;
    private BookingSeatStatus status;
}

