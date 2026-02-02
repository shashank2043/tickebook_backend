package org.team11.tickebook.adminservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class RoleElevationRequest {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID requestedBy;
    @Enumerated(EnumType.STRING)
    private Role requestedRole;
    private Role currentRole;
    private String description;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;
    private UUID reviewedBy;
    private LocalDateTime reviewedAt;
    private String remarks;
    private LocalDateTime createdAt;
}
