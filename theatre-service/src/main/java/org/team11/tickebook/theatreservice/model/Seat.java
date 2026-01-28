package org.team11.tickebook.theatreservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    private Long id;
    private String rowLabel;
    private int seatNumber;
    @Enumerated(EnumType.STRING)
    private SeatType seatType;
    private int positionX;
    private int positionY;
    private boolean isAvailable;
    private boolean isActive;
    @ManyToOne
    private Screen screen;
}
