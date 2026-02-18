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
import org.team11.tickebook.theatreservice.controller.ScreenController;
import org.team11.tickebook.theatreservice.dto.request.CreateScreenRequest;
import org.team11.tickebook.theatreservice.model.Screen;
import org.team11.tickebook.theatreservice.service.ScreenService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScreenController.class)
@AutoConfigureMockMvc(addFilters = true)
@ContextConfiguration(classes = TheatreServiceApplication.class)
public class ScreenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScreenService service;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID userId = UUID.randomUUID();

    private Authentication auth() {
        Claims claims = Jwts.claims();
        claims.put("userId", userId.toString());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(claims);
        return authentication;
    }

    // ---------------- CREATE ----------------

    @Test
    void create_shouldReturn200_whenValid() throws Exception {
        Screen screen = new Screen();
        screen.setName("Screen 1");

        when(service.create(any(), any())).thenReturn(screen);

        CreateScreenRequest req = new CreateScreenRequest(
                "Screen 1", 1, 100, UUID.randomUUID()
        );

        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void create_shouldReturn400_whenBodyMissing() throws Exception {
        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenInvalidJson() throws Exception {
        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{bad_json}"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void create_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.create(any(), any())).thenThrow(new RuntimeException());

        CreateScreenRequest req = CreateScreenRequest.builder()
                .name("Screen 1")
                .screenNumber(1)
                .totalSeats(100)
                .theatreId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError());
    }
    @Test
    void create_shouldReturn400_whenNameBlank() throws Exception {
        CreateScreenRequest req = CreateScreenRequest.builder()
                .name("")
                .screenNumber(1)
                .totalSeats(100)
                .theatreId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void create_shouldReturn400_whenNameTooShort() throws Exception {
        CreateScreenRequest req = CreateScreenRequest.builder()
                .name("A")
                .screenNumber(1)
                .totalSeats(100)
                .theatreId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void create_shouldReturn400_whenScreenNumberNull() throws Exception {
        CreateScreenRequest req = CreateScreenRequest.builder()
                .name("Screen 1")
                .totalSeats(100)
                .theatreId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void create_shouldReturn400_whenScreenNumberNegative() throws Exception {
        CreateScreenRequest req = CreateScreenRequest.builder()
                .name("Screen 1")
                .screenNumber(-1)
                .totalSeats(100)
                .theatreId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void create_shouldReturn400_whenTotalSeatsTooHigh() throws Exception {
        CreateScreenRequest req = CreateScreenRequest.builder()
                .name("Screen 1")
                .screenNumber(1)
                .totalSeats(2000)
                .theatreId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void create_shouldReturn400_whenTheatreIdMissing() throws Exception {
        CreateScreenRequest req = CreateScreenRequest.builder()
                .name("Screen 1")
                .screenNumber(1)
                .totalSeats(100)
                .build();

        mockMvc.perform(post("/api/screens")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }


    // ---------------- GET BY THEATRE ----------------

    @Test
    void getByTheatre_shouldReturn200_whenListExists() throws Exception {
        when(service.getByTheatre(any(), any()))
                .thenReturn(List.of(new Screen(), new Screen()));

        mockMvc.perform(get("/api/screens/theatre/{id}", UUID.randomUUID())
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void getByTheatre_shouldReturn200_whenEmptyList() throws Exception {
        when(service.getByTheatre(any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/screens/theatre/{id}", UUID.randomUUID())
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void getByTheatre_shouldReturn400_whenInvalidUUID() throws Exception {
        mockMvc.perform(get("/api/screens/theatre/{id}", "bad-uuid")
                        .principal(auth()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByTheatre_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.getByTheatre(any(), any())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/screens/theatre/{id}", UUID.randomUUID())
                        .principal(auth()))
                .andExpect(status().isInternalServerError());
    }

    // ---------------- AUTH EDGE ----------------

//    @Test
//    void create_shouldFail_whenNoAuth() throws Exception {
//        CreateScreenRequest req = new CreateScreenRequest(
//                "Screen", 1, 100, UUID.randomUUID()
//        );
//
//        mockMvc.perform(post("/api/screens")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isInternalServerError());
//    }
}

