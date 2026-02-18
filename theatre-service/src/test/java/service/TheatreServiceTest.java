package service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.team11.tickebook.theatreservice.exception.OwnerProfileNotFoundException;
import org.team11.tickebook.theatreservice.exception.TheatreNotFoundException;
import org.team11.tickebook.theatreservice.model.*;
import org.team11.tickebook.theatreservice.repository.*;
import org.team11.tickebook.theatreservice.service.impl.TheatreServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TheatreServiceTest {

    @Mock TheatreRepository theatreRepo;
    @Mock TheatreOwnerProfileRepository ownerRepo;
    @Mock Authentication authentication;
    @Mock Claims claims;

    @InjectMocks
    TheatreServiceImpl service;

    UUID userId;
    TheatreOwnerProfile owner;
    Theatre theatre;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();

        owner = new TheatreOwnerProfile();
        owner.setId(UUID.randomUUID());
        owner.setUserId(userId);

        theatre = new Theatre();
        theatre.setId(UUID.randomUUID());
        theatre.setOwnerProfile(owner);
        theatre.setIsActive(true);
    }

    // ---------- CREATE ----------

    @Test
    void create_shouldCreateTheatre_whenValidOwner() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Theatre result = service.create(new Theatre(), authentication);

        assertEquals(owner, result.getOwnerProfile());
        assertTrue(result.getIsActive());
        verify(theatreRepo).save(any());
    }

    @Test
    void create_shouldThrowOwnerProfileNotFound_whenOwnerMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.create(new Theatre(), authentication));
    }
    @Test
    void create_shouldSetTimestampsAndActiveTrue() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Theatre result = service.create(new Theatre(), authentication);

        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertTrue(result.getIsActive());
    }

    // ---------- GET MY THEATRES ----------

    @Test
    void getMyTheatres_shouldReturnList_whenOwnerExists() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findByOwnerProfile(owner))
                .thenReturn(List.of(theatre, new Theatre()));

        List<Theatre> result = service.getMyTheatres(authentication);

        assertEquals(2, result.size());
    }

    @Test
    void getMyTheatres_shouldReturnEmptyList_whenNoTheatresExist() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findByOwnerProfile(owner))
                .thenReturn(Collections.emptyList());

        List<Theatre> result = service.getMyTheatres(authentication);

        assertTrue(result.isEmpty());
    }

    @Test
    void getMyTheatres_shouldThrowOwnerProfileNotFound_whenOwnerMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.getMyTheatres(authentication));
    }

    // ---------- GET ----------

    @Test
    void get_shouldReturnTheatre_whenExists() {

        when(theatreRepo.findById(theatre.getId()))
                .thenReturn(Optional.of(theatre));

        Theatre result = service.get(theatre.getId());

        assertEquals(theatre, result);
    }

    @Test
    void get_shouldThrowTheatreNotFound_whenMissing() {

        when(theatreRepo.findById(theatre.getId()))
                .thenReturn(Optional.empty());

        assertThrows(TheatreNotFoundException.class,
                () -> service.get(theatre.getId()));
    }

    // ---------- DEACTIVATE ----------

    @Test
    void deactivate_shouldDeactivateTheatre_whenValidOwner() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId()))
                .thenReturn(Optional.of(theatre));

        service.deactivate(theatre.getId(), authentication);

        assertFalse(theatre.getIsActive());
        verify(theatreRepo).save(theatre);
    }

    @Test
    void deactivate_shouldThrowIllegalArgument_whenUserNotOwner() {

        TheatreOwnerProfile otherOwner = new TheatreOwnerProfile();
        otherOwner.setId(UUID.randomUUID());
        theatre.setOwnerProfile(otherOwner);

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId()))
                .thenReturn(Optional.of(theatre));

        assertThrows(IllegalArgumentException.class,
                () -> service.deactivate(theatre.getId(), authentication));
    }

    @Test
    void deactivate_shouldThrowTheatreNotFound_whenTheatreMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId()))
                .thenReturn(Optional.empty());

        assertThrows(TheatreNotFoundException.class,
                () -> service.deactivate(theatre.getId(), authentication));
    }
    @Test
    void deactivate_shouldThrowOwnerProfileNotFound_whenOwnerMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.deactivate(theatre.getId(), authentication));
    }
    @Test
    void deactivate_shouldNotSave_whenUserNotOwner() {

        TheatreOwnerProfile otherOwner = new TheatreOwnerProfile();
        otherOwner.setId(UUID.randomUUID());
        theatre.setOwnerProfile(otherOwner);

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId()))
                .thenReturn(Optional.of(theatre));

        assertThrows(IllegalArgumentException.class,
                () -> service.deactivate(theatre.getId(), authentication));

        verify(theatreRepo, never()).save(any());
    }

}
