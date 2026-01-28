package org.team11.tickebook.theatreservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Show {
    @GeneratedValue
    @Id
    private UUID id;
    @OneToOne
    private Screen screen;
    @OneToOne
    private Movie movie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
