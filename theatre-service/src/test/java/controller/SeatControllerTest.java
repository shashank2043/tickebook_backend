package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.team11.tickebook.theatreservice.TheatreServiceApplication;
import org.team11.tickebook.theatreservice.controller.SeatController;
import org.team11.tickebook.theatreservice.dto.request.CreateSeatRequest;
import org.team11.tickebook.theatreservice.model.Seat;
import org.team11.tickebook.theatreservice.model.SeatType;
import org.team11.tickebook.theatreservice.service.SeatService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeatController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = TheatreServiceApplication.class)
public class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SeatService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Authentication auth() {
        Claims claims = Jwts.claims();
        claims.put("userId", "test-user");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(claims);
        return authentication;
    }

    // ---------------- CREATE ----------------

    @Test
    void create_shouldReturn200_whenValid() throws Exception {
        Seat seat = new Seat();
        seat.setId(1L);

        when(service.create(any(), any())).thenReturn(seat);

        CreateSeatRequest req = CreateSeatRequest.builder()
                .screenId(1L)
                .rowLabel("A")
                .seatNumber(1)
                .seatType(SeatType.SILVER)
                .positionX(10)
                .positionY(10)
                .build();

        mockMvc.perform(post("/api/seats")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void create_shouldReturn400_whenBodyMissing() throws Exception {
        mockMvc.perform(post("/api/seats")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenInvalidJson() throws Exception {
        mockMvc.perform(post("/api/seats")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{bad_json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenValidationFails() throws Exception {
        CreateSeatRequest req = CreateSeatRequest.builder()
                .screenId(null) // invalid
                .rowLabel("")
                .seatNumber(-1)
                .build();

        mockMvc.perform(post("/api/seats")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.create(any(), any())).thenThrow(new RuntimeException());

        CreateSeatRequest req = CreateSeatRequest.builder()
                .screenId(1L)
                .rowLabel("A")
                .seatNumber(1)
                .seatType(SeatType.SILVER)
                .positionX(5)
                .positionY(5)
                .build();

        mockMvc.perform(post("/api/seats")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError());
    }

    // ---------------- GET BY SCREEN ----------------

    @Test
    void getByScreen_shouldReturn200_whenSeatsExist() throws Exception {
        when(service.getByScreen(any(), any()))
                .thenReturn(List.of(new Seat(), new Seat()));

        mockMvc.perform(get("/api/seats/screen/{id}", 1L)
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void getByScreen_shouldReturn200_whenEmptyList() throws Exception {
        when(service.getByScreen(any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/seats/screen/{id}", 1L)
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void getByScreen_shouldReturn400_whenInvalidId() throws Exception {
        mockMvc.perform(get("/api/seats/screen/{id}", "bad")
                .principal(auth()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByScreen_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.getByScreen(any(), any())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/seats/screen/{id}", 1L)
                        .principal(auth()))
                .andExpect(status().isInternalServerError());
    }

    // ---------------- DELETE ----------------

    @Test
    void deactivate_shouldReturn200_whenSuccess() throws Exception {
        mockMvc.perform(delete("/api/seats/{id}", 1L)
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void deactivate_shouldReturn400_whenInvalidId() throws Exception {
        mockMvc.perform(delete("/api/seats/{id}", "bad")
                        .principal(auth()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deactivate_shouldReturn500_whenServiceThrows() throws Exception {
        doThrow(new RuntimeException())
                .when(service).deactivate(any(), any());

        mockMvc.perform(delete("/api/seats/{id}", 1L)
                        .principal(auth()))
                .andExpect(status().isInternalServerError());
    }
}
