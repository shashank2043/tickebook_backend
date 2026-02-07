package org.team11.tickebook.auth_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.auth_service.model.enums.Role;


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
