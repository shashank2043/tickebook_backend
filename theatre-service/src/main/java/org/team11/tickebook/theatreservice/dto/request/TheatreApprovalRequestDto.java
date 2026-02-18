package org.team11.tickebook.theatreservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.theatreservice.model.ApprovalStatus;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TheatreApprovalRequestDto {
    @NotNull(message = "Theatre ID is required")
    private UUID theatreId;
    @NotNull(message = "Theatre Owner Profile ID is required")
    private UUID theatreOwnerProfileId;
    private ApprovalStatus status;
    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;
}
