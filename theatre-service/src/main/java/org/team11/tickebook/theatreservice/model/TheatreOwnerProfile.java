package org.team11.tickebook.theatreservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheatreOwnerProfile {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;
    private String businessName;
    private String businessEmail;
    private String businessAddress;
    private Boolean isVerified;

    @OneToMany(
            mappedBy = "ownerProfile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Theatre> theatres;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

