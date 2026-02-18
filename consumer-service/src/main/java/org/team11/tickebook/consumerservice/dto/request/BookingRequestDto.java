package org.team11.tickebook.consumerservice.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
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
    @Null(message = "userId must be null")
    private UUID userId;
    @NotNull(message = "showId cannot be Null")
    private UUID showId;
    @NotEmpty(message = "seatIds cannot be empty")
    private List<
            @NotNull(message = "seatId cannot be null")
            @Positive(message = "seatId must be positive")
                    Long> seatId;
//    private List<ShowSeat> seats;
}

