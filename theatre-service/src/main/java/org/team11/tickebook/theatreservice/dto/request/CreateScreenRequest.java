package org.team11.tickebook.theatreservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class CreateScreenRequest {
    private String name;
    private Integer screenNumber;
    private Integer totalSeats;
    private UUID theatreId;
}
