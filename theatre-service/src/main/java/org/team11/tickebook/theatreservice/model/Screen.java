package org.team11.tickebook.theatreservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Screen {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer screenNumber;
    private Integer totalSeats;
    private Boolean isActive;
    @ManyToOne
    private Theatre theatre;
    @OneToMany
    private List<Seat> seats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
