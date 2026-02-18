package org.team11.tickebook.consumerservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue
    private UUID id;
    @OneToOne
    private Booking booking;
    private String qrCode;
    private LocalDateTime issuedAt;
}
