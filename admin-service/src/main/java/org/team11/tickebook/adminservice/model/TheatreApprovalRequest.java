package org.team11.tickebook.adminservice.model;

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
public class TheatreApprovalRequest {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID theaterId;
    private UUID theaterOwnerProfileId;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;
    private UUID OwnerProfile;

    @OneToOne
    @JoinColumn(name="admin_id")
    private AdminProfile reviewedBy;

    private LocalDateTime reviewedAt;
    private String remarks;
    private LocalDateTime createdAt;

}
