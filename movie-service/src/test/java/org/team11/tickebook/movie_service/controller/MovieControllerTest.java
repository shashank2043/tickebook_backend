package org.team11.tickebook.movie_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.team11.tickebook.movie_service.dto.request.MovieRegisterDto;
import org.team11.tickebook.movie_service.dto.response.MovieResponseDto;
import org.team11.tickebook.movie_service.service.MovieService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController controller;

    // ================= CREATE =================

    @Test
    void createMovie_shouldReturnResponse_whenValidRequest() {

        MovieRegisterDto request = buildRequest();
        MovieResponseDto responseDto = new MovieResponseDto();
        responseDto.setTitle("Test Movie");

        when(movieService.createMovie(request)).thenReturn(responseDto);

        ResponseEntity<MovieResponseDto> response =
                controller.createMovie(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());

        verify(movieService).createMovie(request);
    }

    @Test
    void createMovie_shouldPropagateException_whenServiceFails() {

        MovieRegisterDto request = buildRequest();

        when(movieService.createMovie(request))
                .thenThrow(new RuntimeException("Failed"));

        assertThrows(RuntimeException.class,
                () -> controller.createMovie(request));

        verify(movieService).createMovie(request);
    }

    // ================= UPDATE =================

    @Test
    void updateMovie_shouldReturnUpdatedMovie_whenValid() {

        UUID id = UUID.randomUUID();
        MovieRegisterDto request = buildRequest();

        MovieResponseDto responseDto = new MovieResponseDto();
        responseDto.setId(id);

        when(movieService.updateMovie(id, request)).thenReturn(responseDto);

        ResponseEntity<MovieResponseDto> response =
                controller.updateMovie(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());

        verify(movieService).updateMovie(id, request);
    }

    @Test
    void updateMovie_shouldPropagateException_whenServiceFails() {

        UUID id = UUID.randomUUID();
        MovieRegisterDto request = buildRequest();

        when(movieService.updateMovie(id, request))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> controller.updateMovie(id, request));
    }

    // ================= GET BY ID =================

    @Test
    void getMovie_shouldReturnMovie_whenExists() {

        UUID id = UUID.randomUUID();

        MovieResponseDto dto = new MovieResponseDto();
        dto.setId(id);

        when(movieService.getMovieById(id)).thenReturn(dto);

        ResponseEntity<MovieResponseDto> response =
                controller.getMovie(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());

        verify(movieService).getMovieById(id);
    }

    @Test
    void getMovie_shouldPropagateException_whenNotFound() {

        UUID id = UUID.randomUUID();

        when(movieService.getMovieById(id))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> controller.getMovie(id));
    }

    // ================= GET ALL =================

    @Test
    void getAllMovies_shouldReturnList() {

        List<MovieResponseDto> list =
                List.of(new MovieResponseDto(), new MovieResponseDto());

        when(movieService.getAllMovies()).thenReturn(list);

        ResponseEntity<List<MovieResponseDto>> response =
                controller.getAllMovies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());

        verify(movieService).getAllMovies();
    }

    @Test
    void getAllMovies_shouldReturnEmptyList_whenNoMovies() {

        when(movieService.getAllMovies()).thenReturn(List.of());

        ResponseEntity<List<MovieResponseDto>> response =
                controller.getAllMovies();

        assertTrue(response.getBody().isEmpty());
    }

    // ================= DELETE =================

    @Test
    void deleteMovie_shouldCallService_andReturnNoContent() {

        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response =
                controller.deleteMovie(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(movieService).deleteMovie(id);
    }

    @Test
    void deleteMovie_shouldPropagateException_whenServiceFails() {

        UUID id = UUID.randomUUID();

        doThrow(new RuntimeException())
                .when(movieService).deleteMovie(id);

        assertThrows(RuntimeException.class,
                () -> controller.deleteMovie(id));

        verify(movieService).deleteMovie(id);
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