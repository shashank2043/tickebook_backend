package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.team11.tickebook.theatreservice.TheatreServiceApplication;
import org.team11.tickebook.theatreservice.controller.InternalController;
import org.team11.tickebook.theatreservice.dto.response.SeatDto;
import org.team11.tickebook.theatreservice.service.OwnerProfileService;
import org.team11.tickebook.theatreservice.service.SeatService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternalController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = TheatreServiceApplication.class)
public class InternalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerProfileService ownerProfileService;

    @MockitoBean
    private SeatService seatService;

    @Autowired
    private ObjectMapper objectMapper;

    private Authentication auth() {
        Claims claims = Jwts.claims();
        claims.put("userId", "test-user");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(claims);
        return authentication;
    }

    // ---------------- VERIFY OWNER ----------------

    @Test
    void verifyOwner_shouldReturn200_whenSuccess() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(put("/internal/owner/{id}/verify", id))
                .andExpect(status().isOk());

        verify(ownerProfileService).verifyOwner(id);
    }

    @Test
    void verifyOwner_shouldReturn400_whenInvalidUUID() throws Exception {
        mockMvc.perform(put("/internal/owner/{id}/verify", "bad-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyOwner_shouldReturn500_whenServiceThrows() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException())
                .when(ownerProfileService).verifyOwner(any());

        mockMvc.perform(put("/internal/owner/{id}/verify", id))
                .andExpect(status().isInternalServerError());
    }

    // ---------------- GET SEATS BY SCREEN ----------------

    @Test
    void getByScreen_shouldReturn200_whenListExists() throws Exception {
        when(seatService.getByScreenAsDto(any(), any()))
                .thenReturn(List.of(new SeatDto(), new SeatDto()));

        mockMvc.perform(get("/internal/seats/screen/{id}", 1L)
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void getByScreen_shouldReturn200_whenEmptyList() throws Exception {
        when(seatService.getByScreenAsDto(any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/internal/seats/screen/{id}", 1L)
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void getByScreen_shouldReturn400_whenInvalidId() throws Exception {
        mockMvc.perform(get("/internal/seats/screen/{id}", "bad")
                        .principal(auth()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByScreen_shouldReturn500_whenServiceThrows() throws Exception {
        when(seatService.getByScreenAsDto(any(), any()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/internal/seats/screen/{id}", 1L)
                        .principal(auth()))
                .andExpect(status().isInternalServerError());
    }
}

