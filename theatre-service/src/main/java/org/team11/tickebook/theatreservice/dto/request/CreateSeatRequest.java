package org.team11.tickebook.theatreservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.team11.tickebook.theatreservice.model.SeatType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSeatRequest {

    @NotNull(message = "Screen ID is required")
    @Positive(message = "Screen ID must be positive")
    private Long screenId;

    @NotBlank(message = "Row label is required")
    @Size(min = 1, max = 5, message = "Row label must be between 1 and 5 characters")
    private String rowLabel;

    @Positive(message = "Seat number must be greater than 0")
    @Max(value = 500, message = "Seat number cannot exceed 500")
    private int seatNumber;

    @NotNull(message = "Seat type is required")
    private SeatType seatType;

    @PositiveOrZero(message = "Position X must be 0 or greater")
    @Max(value = 1000, message = "Position X too large")
    private int positionX;

    @PositiveOrZero(message = "Position Y must be 0 or greater")
    @Max(value = 1000, message = "Position Y too large")
    private int positionY;
}
