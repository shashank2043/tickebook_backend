package org.team11.tickebook.theatreservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private UUID user_id;
    private String businessName;
    private String businessEmail;
    private String businessAddress;
    private Boolean isVerified;
    @OneToMany
    private List<Theatre> theatres;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
