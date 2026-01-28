package org.team11.tickebook.theatreservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Theatre {
    @Id
    @GeneratedValue
    private UUID id;
    private String city;
    private String name;
    private Boolean isActive;
    @ManyToOne
    private TheatreOwnerProfile ownerProfile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
