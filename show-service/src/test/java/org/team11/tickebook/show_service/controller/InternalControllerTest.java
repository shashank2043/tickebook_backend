package org.team11.tickebook.show_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.service.ShowSeatService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InternalControllerTest {

    @Mock
    private ShowSeatService seatService;

    @InjectMocks
    private InternalController controller;

    // ================= LOCK =================

    @Test
    void lockSeat_shouldReturnSeat_whenSuccessful() {

        ShowSeat request = new ShowSeat();
        ShowSeat responseSeat = new ShowSeat();

        when(seatService.lockSeat(request)).thenReturn(responseSeat);

        ShowSeat result = controller.lockSeat(request);

        assertEquals(responseSeat, result);
        verify(seatService).lockSeat(request);
    }

    @Test
    void lockSeat_shouldPropagateException_whenServiceFails() {

        ShowSeat request = new ShowSeat();

        when(seatService.lockSeat(request))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> controller.lockSeat(request));

        verify(seatService).lockSeat(request);
    }

    // ================= CONFIRM =================

    @Test
    void confirmSeat_shouldReturnSeat_whenSuccessful() {

        ShowSeat request = new ShowSeat();
        ShowSeat responseSeat = new ShowSeat();

        when(seatService.confirmSeat(request)).thenReturn(responseSeat);

        ShowSeat result = controller.confirmSeat(request);

        assertEquals(responseSeat, result);
        verify(seatService).confirmSeat(request);
    }

    @Test
    void confirmSeat_shouldPropagateException_whenServiceFails() {

        ShowSeat request = new ShowSeat();

        when(seatService.confirmSeat(request))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> controller.confirmSeat(request));

        verify(seatService).confirmSeat(request);
    }

    // ================= RELEASE =================

    @Test
    void releaseSeat_shouldReturnSeat_whenSuccessful() {

        ShowSeat request = new ShowSeat();
        ShowSeat responseSeat = new ShowSeat();

        when(seatService.releaseSeat(request)).thenReturn(responseSeat);

        ShowSeat result = controller.releaseSeat(request);

        assertEquals(responseSeat, result);
        verify(seatService).releaseSeat(request);
    }

    @Test
    void releaseSeat_shouldPropagateException_whenServiceFails() {

        ShowSeat request = new ShowSeat();

        when(seatService.releaseSeat(request))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> controller.releaseSeat(request));

        verify(seatService).releaseSeat(request);
    }
}