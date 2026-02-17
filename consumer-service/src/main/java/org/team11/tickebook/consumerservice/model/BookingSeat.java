package org.team11.tickebook.consumerservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bookingseat")
public class BookingSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID  id;
    @ManyToOne
    private Booking booking;
    private  Long seatId;
    private  UUID showId;
    private BigDecimal seatPrice;
    @Enumerated(EnumType.STRING)
    private BookingSeatStatus status;
}
