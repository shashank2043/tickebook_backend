package org.team11.tickebook.consumerservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    @Column(name = "show_id")
    private UUID show;
    private  String bookingNumber;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private  PaymentStatus paymentStatus;
    private LocalDateTime bookedAt;
    private LocalDateTime expiresAt;
    private  LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingSeat> seats;
}
