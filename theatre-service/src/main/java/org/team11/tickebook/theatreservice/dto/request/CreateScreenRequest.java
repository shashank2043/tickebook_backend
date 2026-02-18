package org.team11.tickebook.theatreservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
@Builder
public class CreateScreenRequest {
    @NotBlank(message = "Screen name is required")
    @Size(min = 2, max = 50, message = "Screen name must be between 2 and 50 characters")
    private String name;
    @NotNull(message = "Screen number is required")
    @Positive(message = "Screen number must be greater than 0")
    @Max(value = 100, message = "Screen number cannot exceed 100")
    private Integer screenNumber;
    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be greater than 0")
    @Max(value = 1000, message = "Total seats cannot exceed 1000")
    private Integer totalSeats;
    @NotNull(message = "Theatre ID is required")
    private UUID theatreId;
}
