package org.team11.tickebook.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.ChangeType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheatreChangeResponseDto {

    private UUID id;
    private UUID theatreId;
    private ChangeType changeType;
    private String oldValue;
    private String newValue;
    private ApprovalStatus status;

    private UUID requestedBy;
    private UUID reviewedBy;
    private LocalDateTime reviewedAt;
    private String remarks;
    private LocalDateTime createdAt;


}
