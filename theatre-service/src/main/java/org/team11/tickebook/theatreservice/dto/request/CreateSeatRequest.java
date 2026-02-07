package org.team11.tickebook.theatreservice.dto.request;

import lombok.Data;
import org.team11.tickebook.theatreservice.model.SeatType;

@Data
public class CreateSeatRequest {
    private Long screenId;
    private String rowLabel;
    private int seatNumber;
    private SeatType seatType;
    private int positionX;
    private int positionY;
}

