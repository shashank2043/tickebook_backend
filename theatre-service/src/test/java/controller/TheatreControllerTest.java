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
import org.team11.tickebook.theatreservice.controller.TheatreController;
import org.team11.tickebook.theatreservice.model.Theatre;
import org.team11.tickebook.theatreservice.service.TheatreService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TheatreController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = TheatreServiceApplication.class)
class TheatreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TheatreService service;

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
        Theatre theatre = Theatre.builder()
                .city("Hyderabad")
                .name("PVR")
                .isActive(true)
                .build();

        when(service.create(any(), any())).thenReturn(theatre);

        mockMvc.perform(post("/api/theatres")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(theatre)))
                .andExpect(status().isOk());
    }

    @Test
    void create_shouldReturn400_whenCityBlank() throws Exception {
        Theatre theatre = Theatre.builder()
                .city("")
                .name("PVR")
                .build();

        mockMvc.perform(post("/api/theatres")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(theatre)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenNameTooShort() throws Exception {
        Theatre theatre = Theatre.builder()
                .city("Hyderabad")
                .name("A")
                .build();

        mockMvc.perform(post("/api/theatres")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(theatre)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenInvalidJson() throws Exception {
        mockMvc.perform(post("/api/theatres")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{bad_json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.create(any(), any())).thenThrow(new RuntimeException());

        Theatre theatre = Theatre.builder()
                .city("Hyderabad")
                .name("PVR")
                .build();

        mockMvc.perform(post("/api/theatres")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(theatre)))
                .andExpect(status().isInternalServerError());
    }

    // ---------------- GET BY ID ----------------

    @Test
    void get_shouldReturn200_whenExists() throws Exception {
        when(service.get(any())).thenReturn(new Theatre());

        mockMvc.perform(get("/api/theatres/{id}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void get_shouldReturn400_whenInvalidUUID() throws Exception {
        mockMvc.perform(get("/api/theatres/{id}", "bad-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.get(any())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/theatres/{id}", UUID.randomUUID()))
                .andExpect(status().isInternalServerError());
    }

    // ---------------- MY THEATRES ----------------

    @Test
    void myTheatres_shouldReturn200_whenListExists() throws Exception {
        when(service.getMyTheatres(any()))
                .thenReturn(List.of(new Theatre(), new Theatre()));

        mockMvc.perform(get("/api/theatres/me")
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void myTheatres_shouldReturn200_whenEmptyList() throws Exception {
        when(service.getMyTheatres(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/theatres/me")
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void myTheatres_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.getMyTheatres(any())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/theatres/me")
                        .principal(auth()))
                .andExpect(status().isInternalServerError());
    }

    // ---------------- DELETE ----------------

    @Test
    void deactivate_shouldReturn200_whenSuccess() throws Exception {
        mockMvc.perform(delete("/api/theatres/{id}", UUID.randomUUID())
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void deactivate_shouldReturn400_whenInvalidUUID() throws Exception {
        mockMvc.perform(delete("/api/theatres/{id}", "bad"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deactivate_shouldReturn500_whenServiceThrows() throws Exception {
        doThrow(new RuntimeException())
                .when(service).deactivate(any(), any());

        mockMvc.perform(delete("/api/theatres/{id}", UUID.randomUUID())
                        .principal(auth()))
                .andExpect(status().isInternalServerError());
    }
}

