package org.team11.tickebook.adminservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class AdminProfile {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;
    private String region;
    private Boolean canApproveRoles;
    private Boolean canApproveTheaters;
    private Boolean canApproveChanges;
    private LocalDateTime lastActionAt;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
