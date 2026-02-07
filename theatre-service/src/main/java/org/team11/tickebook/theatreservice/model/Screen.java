package org.team11.tickebook.theatreservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_theatre_screen_number",
                        columnNames = {"theatre_id", "screen_number"}
                )
        }
)
public class Screen {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @Column(name = "screen_number", nullable = false)
    private Integer screenNumber;
    private Integer totalSeats;
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", nullable = false)
    @JsonIgnore
    private Theatre theatre;

    @OneToMany(
            mappedBy = "screen",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Seat> seats;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
