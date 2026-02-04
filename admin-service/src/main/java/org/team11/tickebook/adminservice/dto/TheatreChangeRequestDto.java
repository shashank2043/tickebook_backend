package org.team11.tickebook.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.adminservice.model.ChangeType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TheatreChangeRequestDto {
    private UUID theatreId;
    private ChangeType changeType;
    private String oldValue;
    private String newValue;
    private UUID requestedBy;
}
