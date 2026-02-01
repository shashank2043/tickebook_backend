package org.team11.tickebook.movie_service.service;

import org.team11.tickebook.movie_service.dto.request.MovieRegisterDto;
import org.team11.tickebook.movie_service.dto.response.MovieResponseDto;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    MovieResponseDto createMovie(MovieRegisterDto request);

    MovieResponseDto updateMovie(UUID id, MovieRegisterDto request);

    MovieResponseDto getMovieById(UUID id);

    List<MovieResponseDto> getAllMovies();

    void deleteMovie(UUID id);
}
