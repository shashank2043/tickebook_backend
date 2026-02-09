package org.team11.tickebook.theatreservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"screen_id", "row_label", "seat_number"})},
        indexes = {
                @Index(name = "idx_screen_id", columnList = "screen_id")
        })
public class Seat {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "row_label", nullable = false)
    private String rowLabel;
    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    private int positionX;
    private int positionY;
    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "screen_id", nullable = false)
    @JsonIgnore
    private Screen screen;
}
