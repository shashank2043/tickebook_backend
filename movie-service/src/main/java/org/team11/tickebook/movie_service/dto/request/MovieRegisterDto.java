package org.team11.tickebook.movie_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRegisterDto {
    private String title;
    private String language;
    private Integer durationInMinutes;
    private String genre;
    private String rating;
    private LocalDate releaseDate;
    private String description;
    private String posterUrl;
    private Boolean isActive;
}
