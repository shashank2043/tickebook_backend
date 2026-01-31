package org.team11.tickebook.theatreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.theatreservice.model.ApprovalStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class TheatreApprovalResponseDto {
    private UUID id;
    private UUID theatreId;
    private UUID TheatreOwnerProfileId;
    private ApprovalStatus status;
    private UUID reviewedByAdminId;
    private LocalDateTime reviewedAt;
    private String remarks;
    private LocalDateTime createdAt;
}
