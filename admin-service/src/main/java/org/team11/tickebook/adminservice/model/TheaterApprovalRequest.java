package org.team11.tickebook.adminservice.model;

import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TheaterApprovalRequest {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID theaterId;
    private UUID theaterOwnerProfileId;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @OneToOne
    @JoinColumn(name="admin_id")
    private AdminProfile reviewedBy;
    private LocalDateTime reviewedAt;
    private String remarks;
    private LocalDateTime createdAt;


}
