package org.team11.tickebook.movie_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseDto {
    private UUID id;
    private String title;
    private String language;
    private Integer durationInMinutes;
    private String genre;
    private String rating;
    private LocalDate releaseDate;
    private String description;
    private String posterUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
