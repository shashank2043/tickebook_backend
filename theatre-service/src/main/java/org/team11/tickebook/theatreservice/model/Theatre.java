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
public class Theatre {

    @Id
    @GeneratedValue
    private UUID id;

    private String city;
    private String name;
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_profile_id", nullable = false)
    @JsonIgnore
    private TheatreOwnerProfile ownerProfile;

    @OneToMany(
            mappedBy = "theatre",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Screen> screens;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
