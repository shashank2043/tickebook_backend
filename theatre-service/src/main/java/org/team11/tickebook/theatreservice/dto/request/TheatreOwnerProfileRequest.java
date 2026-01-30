package org.team11.tickebook.theatreservice.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.theatreservice.model.Theatre;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheatreOwnerProfileRequest {
    private UUID userId;
    private String businessName;
    @Email
    private String businessEmail;
    private String businessAddress;
    private List<Theatre> theatres;
}
