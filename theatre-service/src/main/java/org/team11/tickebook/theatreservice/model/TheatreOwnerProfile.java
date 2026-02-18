package org.team11.tickebook.theatreservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheatreOwnerProfile {

    @Id
    @GeneratedValue
    private UUID id;
    @NotNull(message = "User ID cannot be empty")
    private UUID userId;
    @NotNull(message = "Business Name cannot be null")
    @NotBlank(message = "Business Name cannot be empty")
    @Size(min = 3, max = 100, message = "Business name must be between 3 and 100 characters")
    private String businessName;
    @NotNull(message ="Business Email cannot be null")
    @NotBlank(message = "Business Email cannot be empty")
    @Email
    private String businessEmail;
    @NotNull
    @NotBlank(message = "Business address is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
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
    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }
}

