package org.team11.tickebook.theatreservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.theatreservice.model.SeatType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {
    private Long id;
    private String rowLabel;
    private int seatNumber;
    private SeatType seatType;
    private int positionX;
    private int positionY;
    private boolean isActive;
}
