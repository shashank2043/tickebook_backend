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
import org.team11.tickebook.theatreservice.controller.OwnerProfileController;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;
import org.team11.tickebook.theatreservice.service.OwnerProfileService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = TheatreServiceApplication.class)
public class OwnerProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerProfileService service;

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
    void create_shouldReturn200_whenValidRequest() throws Exception {
        TheatreOwnerProfile request = TheatreOwnerProfile.builder()
                .userId(UUID.randomUUID())
                .businessName("ABC Theatres")
                .businessEmail("abc@test.com")
                .businessAddress("Hyderabad City")
                .build();

        TheatreOwnerProfile response = TheatreOwnerProfile.builder()
                .id(UUID.randomUUID())
                .businessName("ABC Theatres")
                .businessEmail("abc@test.com")
                .businessAddress("Hyderabad City")
                .build();

        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/owner")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void create_shouldReturn400_whenBodyMissing() throws Exception {
        mockMvc.perform(post("/api/owner")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenInvalidJson() throws Exception {
        mockMvc.perform(post("/api/owner")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid_json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.create(any())).thenThrow(new RuntimeException());

        TheatreOwnerProfile profile = TheatreOwnerProfile.builder()
                .userId(UUID.randomUUID())
                .businessName("ABC Theatres")
                .businessEmail("abc@test.com")
                .businessAddress("Hyderabad City")
                .build();

        mockMvc.perform(post("/api/owner")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isInternalServerError());
    }
    @Test
    void create_shouldReturn400_whenBusinessNameBlank() throws Exception {
        TheatreOwnerProfile profile = new TheatreOwnerProfile();
        profile.setBusinessName(""); // invalid
        profile.setBusinessEmail("test@test.com");
        profile.setBusinessAddress("Hyderabad");

        mockMvc.perform(post("/api/owner")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void create_shouldReturn400_whenEmailInvalid() throws Exception {
        TheatreOwnerProfile profile = new TheatreOwnerProfile();
        profile.setBusinessName("ABC Theatres");
        profile.setBusinessEmail("wrongEmail"); // invalid
        profile.setBusinessAddress("Hyderabad");

        mockMvc.perform(post("/api/owner")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void create_shouldReturn400_whenAddressTooShort() throws Exception {
        TheatreOwnerProfile profile = new TheatreOwnerProfile();
        profile.setBusinessName("ABC");
        profile.setBusinessEmail("abc@test.com");
        profile.setBusinessAddress("A"); // < 5 chars

        mockMvc.perform(post("/api/owner")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isBadRequest());
    }

    // ---------------- GET ME ----------------

    @Test
    void get_shouldReturn200_whenProfileExists() throws Exception {
        TheatreOwnerProfile profile = new TheatreOwnerProfile();
        profile.setId(UUID.randomUUID());

        when(service.getByUserId(any())).thenReturn(profile);

        mockMvc.perform(get("/api/owner/me")
                        .principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void get_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.getByUserId(any())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/owner/me")
                        .principal(auth()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void get_shouldFail_whenNoAuthentication() throws Exception {
        mockMvc.perform(get("/api/owner/me"))
                .andExpect(status().isInternalServerError());
    }

    // ---------------- APPROVAL REQUEST ----------------

    @Test
    void approvalRequest_shouldReturn200_whenSuccess() throws Exception {
        when(service.requestTheatreApproval(any(), any())).thenReturn(true);

        TheatreApprovalRequestDto dto = TheatreApprovalRequestDto.builder()
                .theatreId(UUID.randomUUID())
                .theatreOwnerProfileId(UUID.randomUUID())
                .remarks("Valid")
                .build();

        mockMvc.perform(post("/api/owner/approval-request")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void approvalRequest_shouldReturn400_whenFailed() throws Exception {
        when(service.requestTheatreApproval(any(), any())).thenReturn(false);

        TheatreApprovalRequestDto dto = new TheatreApprovalRequestDto();

        mockMvc.perform(post("/api/owner/approval-request")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approvalRequest_shouldReturn400_whenBodyMissing() throws Exception {
        mockMvc.perform(post("/api/owner/approval-request")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approvalRequest_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.requestTheatreApproval(any(), any()))
                .thenThrow(new RuntimeException());

        TheatreApprovalRequestDto dto = TheatreApprovalRequestDto.builder()
                .theatreOwnerProfileId(UUID.randomUUID())
                .theatreId(UUID.randomUUID())
                .remarks("ok")
                .build();

        mockMvc.perform(post("/api/owner/approval-request")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }
    @Test
    void approvalRequest_shouldReturn400_whenServiceReturnsFalse() throws Exception {
        when(service.requestTheatreApproval(any(), any())).thenReturn(false);

        TheatreApprovalRequestDto dto = TheatreApprovalRequestDto.builder()
                .theatreId(UUID.randomUUID())
                .theatreOwnerProfileId(UUID.randomUUID())
                .remarks("Valid remarks")
                .build();

        mockMvc.perform(post("/api/owner/approval-request")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approvalRequest_shouldReturn400_whenTheatreIdMissing() throws Exception {
        TheatreApprovalRequestDto dto = TheatreApprovalRequestDto.builder()
                .theatreOwnerProfileId(UUID.randomUUID())
                .remarks("ok")
                .build();

        mockMvc.perform(post("/api/owner/approval-request")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void approvalRequest_shouldReturn400_whenOwnerProfileMissing() throws Exception {
        TheatreApprovalRequestDto dto = TheatreApprovalRequestDto.builder()
                .theatreId(UUID.randomUUID())
                .remarks("ok")
                .build();

        mockMvc.perform(post("/api/owner/approval-request")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void approvalRequest_shouldReturn400_whenRemarksTooLong() throws Exception {
        String longRemarks = "A".repeat(600);

        TheatreApprovalRequestDto dto = TheatreApprovalRequestDto.builder()
                .theatreId(UUID.randomUUID())
                .theatreOwnerProfileId(UUID.randomUUID())
                .remarks(longRemarks)
                .build();

        mockMvc.perform(post("/api/owner/approval-request")
                        .principal(auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ---------------- CHECK STATUS ----------------

    @Test
    void checkStatus_shouldReturn200_whenListExists() throws Exception {
        when(service.checkStatus(any(), any()))
                .thenReturn(List.of(new TheatreApprovalResponseDto()));

        mockMvc.perform(get("/api/owner/approval-request")
                        .principal(auth())
                        .param("theatreId", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void checkStatus_shouldReturn200_whenEmptyList() throws Exception {
        when(service.checkStatus(any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/owner/approval-request")
                        .principal(auth())
                        .param("theatreId", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void checkStatus_shouldReturn400_whenParamMissing() throws Exception {
        mockMvc.perform(get("/api/owner/approval-request")
                        .principal(auth()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkStatus_shouldReturn500_whenServiceThrows() throws Exception {
        when(service.checkStatus(any(), any()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/owner/approval-request")
                        .principal(auth())
                        .param("theatreId", UUID.randomUUID().toString()))
                .andExpect(status().isInternalServerError());
    }
}
