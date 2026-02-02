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
public class TheatreChangeRequest {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID theaterId;
    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
    private String oldValue;
    private String newValue;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;
    private UUID requestedBy;

    @ManyToOne
    private AdminProfile reviewedBy;
    private LocalDateTime reviewedAt;
    private String remarks;
    private LocalDateTime createdAt;





}
