package org.team11.tickebook.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.auth_service.model.enums.ApprovalStatus;
import org.team11.tickebook.auth_service.model.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleApprovalResponseDto {
    private UUID id;

    private UUID requestedBy;
    private Role currentRole;
    private Role requestedRole;

    private ApprovalStatus status;

    private UUID reviewedByAdminId;
    private LocalDateTime reviewedAt;

    private String remarks;
    private LocalDateTime createdAt;
}
