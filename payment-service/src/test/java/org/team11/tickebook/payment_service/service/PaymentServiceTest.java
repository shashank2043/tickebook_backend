package org.team11.tickebook.payment_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.payment_service.gateway.PaymentGateway;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private PaymentService paymentService;

    // ================= SUCCESS =================

    @Test
    void processPayment_shouldReturnTrue_whenGatewayReturnsSuccess() {

        UUID bookingId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(500);

        when(paymentGateway.processPayment(bookingId, amount))
                .thenReturn(true);

        boolean result = paymentService.processPayment(bookingId, amount);

        assertTrue(result);
        verify(paymentGateway).processPayment(bookingId, amount);
    }

    // ================= FAILURE =================

    @Test
    void processPayment_shouldReturnFalse_whenGatewayReturnsFailure() {

        UUID bookingId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(500);

        when(paymentGateway.processPayment(bookingId, amount))
                .thenReturn(false);

        boolean result = paymentService.processPayment(bookingId, amount);

        assertFalse(result);
        verify(paymentGateway).processPayment(bookingId, amount);
    }

    // ================= EDGE =================

    @Test
    void processPayment_shouldPropagateException_whenGatewayFails() {

        UUID bookingId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(500);

        when(paymentGateway.processPayment(bookingId, amount))
                .thenThrow(new RuntimeException("Gateway error"));

        assertThrows(RuntimeException.class,
                () -> paymentService.processPayment(bookingId, amount));

        verify(paymentGateway).processPayment(bookingId, amount);
    }
}