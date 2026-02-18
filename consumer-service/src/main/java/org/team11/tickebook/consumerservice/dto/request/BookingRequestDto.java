package org.team11.tickebook.consumerservice.dto.request;


import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.consumerservice.dto.ShowSeat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    @Null
    private UUID userId;
    private UUID showId;
    private List<Long> seatId;
//    private List<ShowSeat> seats;
}

