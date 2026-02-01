package org.team11.tickebook.movie_service.service.imple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team11.tickebook.movie_service.dto.request.MovieRegisterDto;
import org.team11.tickebook.movie_service.dto.response.MovieResponseDto;
import org.team11.tickebook.movie_service.exception.MovieNotFoundException;
import org.team11.tickebook.movie_service.model.Movie;
import org.team11.tickebook.movie_service.repository.MovieRepository;
import org.team11.tickebook.movie_service.service.MovieService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    @Override
    public MovieResponseDto createMovie(MovieRegisterDto request) {
        Movie movie = mapToEntity(request);
        movie.setCreatedAt(LocalDateTime.now());
        movie.setUpdatedAt(LocalDateTime.now());
        Movie saved = movieRepository.save(movie);
        return mapToResponse(saved);
    }

    @Override
    public MovieResponseDto updateMovie(UUID id, MovieRegisterDto request) {
        Movie movie = movieRepository
                .findById(id)
                .orElseThrow(
                        ()->new MovieNotFoundException("Movie not found with id: "+id));
        movie.setTitle(request.getTitle());
        movie.setLanguage(request.getLanguage());
        movie.setDurationInMinutes(request.getDurationInMinutes());
        movie.setGenre(request.getGenre());
        movie.setRating(request.getRating());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setDescription(request.getDescription());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setIsActive(request.getIsActive());
        movie.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(movieRepository.save(movie));
    }

    @Override
    public MovieResponseDto getMovieById(UUID id) {
        Movie movie = movieRepository
                .findById(id)
                .orElseThrow(
                        ()->new MovieNotFoundException("Movie not found with id: "+id));

        return mapToResponse(movie);
    }

    @Override
    public List<MovieResponseDto> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMovie(UUID id) {
        movieRepository.deleteById(id);
    }
    private Movie mapToEntity(MovieRegisterDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setLanguage(dto.getLanguage());
        movie.setDurationInMinutes(dto.getDurationInMinutes());
        movie.setGenre(dto.getGenre());
        movie.setRating(dto.getRating());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setDescription(dto.getDescription());
        movie.setPosterUrl(dto.getPosterUrl());
        movie.setIsActive(dto.getIsActive());
        return movie;
    }

    private MovieResponseDto mapToResponse(Movie movie) {
        MovieResponseDto dto = new MovieResponseDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setLanguage(movie.getLanguage());
        dto.setDurationInMinutes(movie.getDurationInMinutes());
        dto.setGenre(movie.getGenre());
        dto.setRating(movie.getRating());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setDescription(movie.getDescription());
        dto.setPosterUrl(movie.getPosterUrl());
        dto.setIsActive(movie.getIsActive());
        dto.setCreatedAt(movie.getCreatedAt());
        dto.setUpdatedAt(movie.getUpdatedAt());
        return dto;
    }
}
