package org.team11.tickebook.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.adminservice.model.Role;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class RoleApprovalRequestDto {
    private UUID requestedBy;
    private Role requestedRole;
    private Role currentRole;
    private String description;
}
