package org.team11.tickebook.movie_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.movie_service.dto.request.MovieRegisterDto;
import org.team11.tickebook.movie_service.dto.response.MovieResponseDto;
import org.team11.tickebook.movie_service.exception.MovieNotFoundException;
import org.team11.tickebook.movie_service.model.Movie;
import org.team11.tickebook.movie_service.repository.MovieRepository;
import org.team11.tickebook.movie_service.service.imple.MovieServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl service;

    // ================= CREATE =================

    @Test
    void createMovie_shouldSaveAndReturnResponse_whenValidRequest() {

        MovieRegisterDto request = buildRequest();

        when(movieRepository.save(any(Movie.class)))
                .thenAnswer(invocation -> {
                    Movie m = invocation.getArgument(0);
                    m.setId(UUID.randomUUID());
                    return m;
                });

        MovieResponseDto result = service.createMovie(request);

        assertNotNull(result);
        assertEquals(request.getTitle(), result.getTitle());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(movieRepository).save(any(Movie.class));
    }

    // ================= UPDATE =================

    @Test
    void updateMovie_shouldUpdateAndReturnResponse_whenMovieExists() {

        UUID id = UUID.randomUUID();
        MovieRegisterDto request = buildRequest();

        Movie existing = new Movie();
        existing.setId(id);

        when(movieRepository.findById(id)).thenReturn(Optional.of(existing));
        when(movieRepository.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MovieResponseDto result = service.updateMovie(id, request);

        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getLanguage(), result.getLanguage());

        verify(movieRepository).findById(id);
        verify(movieRepository).save(existing);
    }

    @Test
    void updateMovie_shouldThrowException_whenMovieNotFound() {

        UUID id = UUID.randomUUID();
        MovieRegisterDto request = buildRequest();

        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class,
                () -> service.updateMovie(id, request));

        verify(movieRepository).findById(id);
    }

    // ================= GET BY ID =================

    @Test
    void getMovieById_shouldReturnMovie_whenExists() {

        UUID id = UUID.randomUUID();

        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle("Test");

        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));

        MovieResponseDto result = service.getMovieById(id);

        assertEquals(id, result.getId());
        assertEquals("Test", result.getTitle());

        verify(movieRepository).findById(id);
    }

    @Test
    void getMovieById_shouldThrowException_whenNotFound() {

        UUID id = UUID.randomUUID();

        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class,
                () -> service.getMovieById(id));
    }

    // ================= GET ALL =================

    @Test
    void getAllMovies_shouldReturnList() {

        Movie movie1 = new Movie();
        movie1.setId(UUID.randomUUID());
        movie1.setTitle("Movie1");

        Movie movie2 = new Movie();
        movie2.setId(UUID.randomUUID());
        movie2.setTitle("Movie2");

        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        List<MovieResponseDto> result = service.getAllMovies();

        assertEquals(2, result.size());
        assertEquals("Movie1", result.get(0).getTitle());

        verify(movieRepository).findAll();
    }

    @Test
    void getAllMovies_shouldReturnEmptyList_whenNoData() {

        when(movieRepository.findAll()).thenReturn(List.of());

        List<MovieResponseDto> result = service.getAllMovies();

        assertTrue(result.isEmpty());
    }

    // ================= DELETE =================

    @Test
    void deleteMovie_shouldCallRepository() {

        UUID id = UUID.randomUUID();

        service.deleteMovie(id);

        verify(movieRepository).deleteById(id);
    }

    @Test
    void deleteMovie_shouldPropagateException_whenRepositoryFails() {

        UUID id = UUID.randomUUID();

        doThrow(new RuntimeException("Delete failed"))
                .when(movieRepository).deleteById(id);

        assertThrows(RuntimeException.class,
                () -> service.deleteMovie(id));

        verify(movieRepository).deleteById(id);
    }

    // ================= HELPER =================

    private MovieRegisterDto buildRequest() {
        return new MovieRegisterDto(
                "Test Movie",
                "English",
                120,
                "Action",
                "PG-13",
                LocalDate.now(),
                "Description",
                "poster.jpg",
                true
        );
    }
}