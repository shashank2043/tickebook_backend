package service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.team11.tickebook.theatreservice.clients.AdminClient;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;
import org.team11.tickebook.theatreservice.exception.OwnerProfileNotFoundException;
import org.team11.tickebook.theatreservice.model.*;
import org.team11.tickebook.theatreservice.repository.*;
import org.team11.tickebook.theatreservice.service.impl.OwnerProfileServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class OwnerProfileServiceTest {

    @Mock
    TheatreOwnerProfileRepository profileRepository;

    @Mock
    AdminClient adminClient;

    @Mock
    TheatreRepository theatreRepository;

    @Mock
    Authentication authentication;

    @Mock
    Claims claims;

    @InjectMocks
    OwnerProfileServiceImpl service;

    UUID userId;
    TheatreOwnerProfile profile;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        profile = new TheatreOwnerProfile();
        profile.setId(UUID.randomUUID());
        profile.setUserId(userId);
        profile.setIsVerified(false);
    }

    // ---------- CREATE ----------

    @Test
    void create_shouldSetVerificationFalseAndSaveProfile() {
        when(profileRepository.save(any())).thenReturn(profile);

        TheatreOwnerProfile result = service.create(profile);

        assertFalse(result.getIsVerified());
        verify(profileRepository).save(profile);
    }
    @Test
    void create_shouldForceVerificationFalse_evenIfTruePassed() {
        profile.setIsVerified(true);

        when(profileRepository.save(any())).thenReturn(profile);

        TheatreOwnerProfile result = service.create(profile);

        assertFalse(result.getIsVerified());
    }

    // ---------- GET BY USER ----------

    @Test
    void getByUserId_shouldReturnProfile_whenProfileExists() {
        when(profileRepository.findByUserId(userId))
                .thenReturn(Optional.of(profile));

        TheatreOwnerProfile result = service.getByUserId(userId);

        assertEquals(profile, result);
    }

    @Test
    void getByUserId_shouldThrowException_whenProfileNotFound() {
        when(profileRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.getByUserId(userId));
    }

    // ---------- UPDATE ----------

    @Test
    void update_shouldSaveAndReturnProfile_whenProfileExists() {
        when(profileRepository.existsById(profile.getId())).thenReturn(true);
        when(profileRepository.save(profile)).thenReturn(profile);

        TheatreOwnerProfile result = service.update(profile);

        assertEquals(profile, result);
    }

    @Test
    void update_shouldThrowException_whenProfileDoesNotExist() {
        when(profileRepository.existsById(profile.getId())).thenReturn(false);

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.update(profile));
    }

    // ---------- REQUEST APPROVAL ----------

    @Test
    void requestApproval_shouldReturnTrue_whenValidOwnerAndAdminAccepts() {
        UUID theatreId = UUID.randomUUID();

        TheatreApprovalRequestDto req = TheatreApprovalRequestDto.builder()
                .theatreId(theatreId)
                .remarks("Test")
                .build();

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(theatreRepository.existsByIdAndOwnerProfile(theatreId, profile))
                .thenReturn(true);
        when(adminClient.createRequest(any()))
                .thenReturn(ResponseEntity.ok(true));

        Boolean result = service.requestTheatreApproval(authentication, req);

        assertTrue(result);
        verify(adminClient).createRequest(any());
    }

    @Test
    void requestApproval_shouldThrowException_whenUserIsNotOwnerOfTheatre() {
        UUID theatreId = UUID.randomUUID();

        TheatreApprovalRequestDto req = TheatreApprovalRequestDto.builder()
                .theatreId(theatreId)
                .build();

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(theatreRepository.existsByIdAndOwnerProfile(theatreId, profile))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.requestTheatreApproval(authentication, req));
    }

    @Test
    void requestApproval_shouldThrowException_whenProfileNotFound() {
        UUID theatreId = UUID.randomUUID();

        TheatreApprovalRequestDto req = TheatreApprovalRequestDto.builder()
                .theatreId(theatreId)
                .build();

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(profileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.requestTheatreApproval(authentication, req));
    }

    @Test
    void requestApproval_shouldReturnFalse_whenAdminRejectsRequest() {
        UUID theatreId = UUID.randomUUID();

        TheatreApprovalRequestDto req = TheatreApprovalRequestDto.builder()
                .theatreId(theatreId)
                .build();

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(theatreRepository.existsByIdAndOwnerProfile(theatreId, profile))
                .thenReturn(true);
        when(adminClient.createRequest(any()))
                .thenReturn(ResponseEntity.ok(false));

        Boolean result = service.requestTheatreApproval(authentication, req);

        assertFalse(result);
    }

    // ---------- VERIFY OWNER ----------

    @Test
    void verifyOwner_shouldMarkVerifiedAndSave_whenProfileExists() {
        when(profileRepository.findById(profile.getId()))
                .thenReturn(Optional.of(profile));

        service.verifyOwner(profile.getId());

        assertTrue(profile.getIsVerified());
        verify(profileRepository).save(profile);
    }

    @Test
    void verifyOwner_shouldThrowException_whenProfileNotFound() {
        when(profileRepository.findById(profile.getId()))
                .thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.verifyOwner(profile.getId()));
    }
}
