package org.team11.tickebook.adminservice.dto;

import lombok.Data;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
@Data
public class TheatreApprovalReviewDto {
    private ApprovalStatus status;
    private String remarks;
}
