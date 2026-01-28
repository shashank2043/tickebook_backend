package org.team11.tickebook.consumerservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ticket")
public class Ticket {
    @Id
    private UUID id;
    @OneToOne
    private Booking booking;
    private String qrCode;
    private LocalDateTime issuedAt;
}
