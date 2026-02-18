package service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.team11.tickebook.theatreservice.dto.request.CreateSeatRequest;
import org.team11.tickebook.theatreservice.dto.response.SeatDto;
import org.team11.tickebook.theatreservice.exception.OwnerProfileNotFoundException;
import org.team11.tickebook.theatreservice.model.*;
import org.team11.tickebook.theatreservice.repository.*;
import org.team11.tickebook.theatreservice.service.impl.SeatServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {

    @Mock SeatRepository seatRepo;
    @Mock ScreenRepository screenRepo;
    @Mock TheatreOwnerProfileRepository ownerRepo;
    @Mock Authentication authentication;
    @Mock Claims claims;

    @InjectMocks
    SeatServiceImpl service;

    UUID userId;
    TheatreOwnerProfile owner;
    Screen screen;
    Theatre theatre;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();

        owner = new TheatreOwnerProfile();
        owner.setId(UUID.randomUUID());
        owner.setUserId(userId);

        theatre = new Theatre();
        theatre.setOwnerProfile(owner);

        screen = new Screen();
        screen.setId(1L);
        screen.setTheatre(theatre);
    }

    // ---------- CREATE ----------

    @Test
    void create_shouldCreateSeat_whenValidRequest() {

        CreateSeatRequest req = buildRequest();

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(screen.getId())).thenReturn(Optional.of(screen));
        when(seatRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Seat result = service.create(req, authentication);

        assertEquals("A", result.getRowLabel());
        assertTrue(result.isActive());
        verify(seatRepo).save(any());
    }

    @Test
    void create_shouldThrowOwnerProfileNotFound_whenOwnerMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.create(buildRequest(), authentication));
    }

    @Test
    void create_shouldThrowIllegalArgument_whenUserNotTheatreOwner() {

        TheatreOwnerProfile otherOwner = new TheatreOwnerProfile();
        otherOwner.setId(UUID.randomUUID());
        theatre.setOwnerProfile(otherOwner);

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(screen.getId())).thenReturn(Optional.of(screen));

        assertThrows(IllegalArgumentException.class,
                () -> service.create(buildRequest(), authentication));
    }

    @Test
    void create_shouldDefaultSeatActiveTrue_whenCreated() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(screen.getId())).thenReturn(Optional.of(screen));
        when(seatRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Seat result = service.create(buildRequest(), authentication);

        assertTrue(result.isActive());
    }
    @Test
    void create_shouldThrowException_whenScreenNotFound() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.create(buildRequest(), authentication));
    }

    // ---------- GET BY SCREEN ----------

    @Test
    void getByScreen_shouldReturnSeats_whenValidOwner() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(screen.getId())).thenReturn(Optional.of(screen));
        when(seatRepo.findByScreen(screen)).thenReturn(List.of(new Seat(), new Seat()));

        List<Seat> result = service.getByScreen(screen.getId(), authentication);

        assertEquals(2, result.size());
    }

    @Test
    void getByScreen_shouldReturnEmptyList_whenNoSeatsExist() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(screen.getId())).thenReturn(Optional.of(screen));
        when(seatRepo.findByScreen(screen)).thenReturn(Collections.emptyList());

        List<Seat> result = service.getByScreen(screen.getId(), authentication);

        assertTrue(result.isEmpty());
    }
    @Test
    void getByScreen_shouldThrowOwnerProfileNotFound_whenOwnerMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.getByScreen(screen.getId(), authentication));
    }
    @Test
    void getByScreen_shouldThrowException_whenScreenNotFound() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getByScreen(screen.getId(), authentication));
    }
    @Test
    void getByScreen_shouldThrowIllegalArgument_whenUserNotOwner() {

        TheatreOwnerProfile otherOwner = new TheatreOwnerProfile();
        otherOwner.setId(UUID.randomUUID());
        theatre.setOwnerProfile(otherOwner);

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(screen.getId())).thenReturn(Optional.of(screen));

        assertThrows(IllegalArgumentException.class,
                () -> service.getByScreen(screen.getId(), authentication));
    }


    // ---------- DEACTIVATE ----------

    @Test
    void deactivate_shouldDeactivateSeat_whenValidOwner() {

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setScreen(screen);
        seat.setActive(true);

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(seatRepo.findById(1L)).thenReturn(Optional.of(seat));

        service.deactivate(1L, authentication);

        assertFalse(seat.isActive());
        verify(seatRepo).save(seat);
    }

    @Test
    void deactivate_shouldThrowException_whenSeatNotFound() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(seatRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.deactivate(1L, authentication));
    }
    @Test
    void deactivate_shouldThrowOwnerProfileNotFound_whenOwnerMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.deactivate(1L, authentication));
    }
    @Test
    void deactivate_shouldThrowIllegalArgument_whenUserNotOwner() {

        TheatreOwnerProfile otherOwner = new TheatreOwnerProfile();
        otherOwner.setId(UUID.randomUUID());
        theatre.setOwnerProfile(otherOwner);

        Seat seat = buildSeat();
        seat.setScreen(screen);

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(seatRepo.findById(1L)).thenReturn(Optional.of(seat));

        assertThrows(IllegalArgumentException.class,
                () -> service.deactivate(1L, authentication));
    }


    // ---------- DTO ----------

    @Test
    void getByScreenAsDto_shouldMapSeatToDto_whenValid() {

        Seat seat = buildSeat();

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(screen.getId())).thenReturn(Optional.of(screen));
        when(seatRepo.findByScreen(screen)).thenReturn(List.of(seat));

        List<SeatDto> result = service.getByScreenAsDto(screen.getId(), authentication);

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getRowLabel());
    }
    @Test
    void getByScreenAsDto_shouldThrowOwnerProfileNotFound_whenOwnerMissing() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(OwnerProfileNotFoundException.class,
                () -> service.getByScreenAsDto(screen.getId(), authentication));
    }
    @Test
    void getByScreenAsDto_shouldThrowException_whenScreenNotFound() {

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getByScreenAsDto(screen.getId(), authentication));
    }
    @Test
    void getByScreenAsDto_shouldThrowIllegalArgument_whenUserNotOwner() {

        TheatreOwnerProfile otherOwner = new TheatreOwnerProfile();
        otherOwner.setId(UUID.randomUUID());
        theatre.setOwnerProfile(otherOwner);

        when(authentication.getPrincipal()).thenReturn(claims);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());
        when(ownerRepo.findByUserId(userId)).thenReturn(Optional.of(owner));
        when(screenRepo.findById(screen.getId())).thenReturn(Optional.of(screen));

        assertThrows(IllegalArgumentException.class,
                () -> service.getByScreenAsDto(screen.getId(), authentication));
    }

    // ---------- HELPERS ----------

    private CreateSeatRequest buildRequest() {
        CreateSeatRequest req = new CreateSeatRequest();
        req.setScreenId(screen.getId());
        req.setRowLabel("A");
        req.setSeatNumber(1);
        req.setSeatType(SeatType.SILVER);
        req.setPositionX(10);
        req.setPositionY(20);
        return req;
    }

    private Seat buildSeat() {
        Seat seat = new Seat();
        seat.setId(1L);
        seat.setRowLabel("A");
        seat.setSeatNumber(1);
        seat.setSeatType(SeatType.SILVER);
        seat.setPositionX(10);
        seat.setPositionY(20);
        seat.setActive(true);
        seat.setScreen(screen);
        return seat;
    }
}
