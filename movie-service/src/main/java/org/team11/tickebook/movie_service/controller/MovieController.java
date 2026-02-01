package org.team11.tickebook.movie_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.movie_service.dto.request.MovieRegisterDto;
import org.team11.tickebook.movie_service.dto.response.MovieResponseDto;
import org.team11.tickebook.movie_service.service.MovieService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    @PostMapping
    public ResponseEntity<MovieResponseDto> createMovie(@RequestBody MovieRegisterDto request) {
        return ResponseEntity.ok(movieService.createMovie(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDto> updateMovie(
            @PathVariable UUID id,
            @RequestBody MovieRegisterDto request) {
        return ResponseEntity.ok(movieService.updateMovie(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovie(@PathVariable UUID id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }
    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
