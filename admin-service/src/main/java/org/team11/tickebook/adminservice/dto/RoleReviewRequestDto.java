package org.team11.tickebook.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.adminservice.model.ApprovalStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleReviewRequestDto {

    private ApprovalStatus status;
    private String remarks;
}

