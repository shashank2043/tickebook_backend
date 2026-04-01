package org.team11.tickebook.payment_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.payment_service.model.PaymentRequest;
import org.team11.tickebook.payment_service.model.PaymentResponse;
import org.team11.tickebook.payment_service.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InternalPaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private InternalPaymentController controller;

    // ================= SUCCESS =================

    @Test
    void process_shouldReturnSuccessResponse_whenPaymentSucceeds() {

        UUID bookingId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(500);

        PaymentRequest request = new PaymentRequest(bookingId, amount);

        when(paymentService.processPayment(bookingId, amount))
                .thenReturn(true);

        PaymentResponse response = controller.process(request);

        assertTrue(response.success());
        assertEquals("Payment successful", response.message());

        verify(paymentService).processPayment(bookingId, amount);
    }

    // ================= FAILURE =================

    @Test
    void process_shouldReturnFailureResponse_whenPaymentFails() {

        UUID bookingId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(500);

        PaymentRequest request = new PaymentRequest(bookingId, amount);

        when(paymentService.processPayment(bookingId, amount))
                .thenReturn(false);

        PaymentResponse response = controller.process(request);

        assertFalse(response.success());
        assertEquals("Payment failed", response.message());

        verify(paymentService).processPayment(bookingId, amount);
    }

    // ================= EXCEPTION =================

    @Test
    void process_shouldPropagateException_whenServiceFails() {

        UUID bookingId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(500);

        PaymentRequest request = new PaymentRequest(bookingId, amount);

        when(paymentService.processPayment(bookingId, amount))
                .thenThrow(new RuntimeException("Error"));

        assertThrows(RuntimeException.class,
                () -> controller.process(request));

        verify(paymentService).processPayment(bookingId, amount);
    }
}