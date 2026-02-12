package service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.team11.tickebook.theatreservice.dto.request.CreateScreenRequest;
import org.team11.tickebook.theatreservice.exception.OwnerProfileNotFoundException;
import org.team11.tickebook.theatreservice.exception.TheatreNotFoundException;
import org.team11.tickebook.theatreservice.model.*;
import org.team11.tickebook.theatreservice.repository.*;
import org.team11.tickebook.theatreservice.service.impl.ScreenServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreenServiceTest {

    @Mock
    ScreenRepository screenRepo;
    @Mock
    TheatreRepository theatreRepo;
    @Mock
    TheatreOwnerProfileRepository ownerRepo;
    @Mock
    Authentication authentication;
    @Mock
    Claims claims;

    @InjectMocks
    ScreenServiceImpl service;

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
    }

    // ---------- CREATE ----------

    @Test
    void create_shouldCreateScreen_whenValidRequest() {

        CreateScreenRequest req = new CreateScreenRequest(
                "Screen 1",
                1,
                100,
                theatre.getId()
        );

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId())).thenReturn(Optional.of(theatre));
        when(screenRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Screen result = service.create(req, authentication);

        assertEquals("Screen 1", result.getName());
        assertTrue(result.getIsActive());
        verify(screenRepo).save(any());
    }

    @Test
    void create_shouldThrowOwnerProfileNotFound_whenOwnerDoesNotExist() {

        CreateScreenRequest req = new CreateScreenRequest(
                "S", 1, 10, UUID.randomUUID()
        );

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.create(req, authentication));
    }

    @Test
    void create_shouldThrowTheatreNotFound_whenTheatreDoesNotExist() {

        CreateScreenRequest req = new CreateScreenRequest(
                "S", 1, 10, UUID.randomUUID()
        );

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(any())).thenReturn(Optional.empty());

        assertThrows(TheatreNotFoundException.class,
                () -> service.create(req, authentication));
    }

    @Test
    void create_shouldThrowIllegalArgument_whenUserIsNotTheatreOwner() {

        TheatreOwnerProfile otherOwner = new TheatreOwnerProfile();
        otherOwner.setId(UUID.randomUUID());
        theatre.setOwnerProfile(otherOwner);

        CreateScreenRequest req = new CreateScreenRequest(
                "S", 1, 10, theatre.getId()
        );

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId())).thenReturn(Optional.of(theatre));

        assertThrows(IllegalArgumentException.class,
                () -> service.create(req, authentication));
    }

    @Test
    void create_shouldDefaultScreenActiveTrue_whenCreated() {

        CreateScreenRequest req = new CreateScreenRequest(
                "Screen X", 2, 50, theatre.getId()
        );

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId())).thenReturn(Optional.of(theatre));
        when(screenRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Screen result = service.create(req, authentication);

        assertTrue(result.getIsActive());
    }

    // ---------- GET BY THEATRE ----------

    @Test
    void getByTheatre_shouldReturnScreens_whenOwnerValid() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId())).thenReturn(Optional.of(theatre));
        when(screenRepo.findByTheatre(theatre))
                .thenReturn(List.of(new Screen(), new Screen()));

        List<Screen> result = service.getByTheatre(theatre.getId(), authentication);

        assertEquals(2, result.size());
    }

    @Test
    void getByTheatre_shouldThrowIllegalArgument_whenUserNotOwner() {

        TheatreOwnerProfile otherOwner = new TheatreOwnerProfile();
        otherOwner.setId(UUID.randomUUID());
        theatre.setOwnerProfile(otherOwner);

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId())).thenReturn(Optional.of(theatre));

        assertThrows(IllegalArgumentException.class,
                () -> service.getByTheatre(theatre.getId(), authentication));
    }

    @Test
    void getByTheatre_shouldThrowOwnerProfileNotFound_whenOwnerMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.getByTheatre(theatre.getId(), authentication));
    }

    @Test
    void getByTheatre_shouldThrowTheatreNotFound_whenTheatreMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(any())).thenReturn(Optional.empty());

        assertThrows(TheatreNotFoundException.class,
                () -> service.getByTheatre(theatre.getId(), authentication));
    }

    @Test
    void getByTheatre_shouldReturnEmptyList_whenNoScreensExist() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(theatreRepo.findById(theatre.getId())).thenReturn(Optional.of(theatre));
        when(screenRepo.findByTheatre(theatre)).thenReturn(Collections.emptyList());

        List<Screen> result = service.getByTheatre(theatre.getId(), authentication);

        assertTrue(result.isEmpty());
    }
}
