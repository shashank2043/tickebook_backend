package org.team11.tickebook.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.adminservice.model.ApprovalStatus;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TheatreApprovalRequestDto {
    private UUID theatreId;
    private UUID theatreOwnerProfileId;
    private ApprovalStatus status;
    private String remarks;
}
