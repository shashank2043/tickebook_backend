package org.team11.tickebook.theatreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.theatreservice.model.Theatre;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheatreOwnerProfileResponse {
    private String businessName;
    private String businessEmail;
    private String businessAddress;
    private List<Theatre> theatres;
}
