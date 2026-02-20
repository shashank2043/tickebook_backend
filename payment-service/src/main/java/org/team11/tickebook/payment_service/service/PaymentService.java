package org.team11.tickebook.payment_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team11.tickebook.payment_service.gateway.PaymentGateway;
import org.team11.tickebook.payment_service.model.Payment;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentGateway paymentGateway;

    public boolean processPayment(UUID bookingId, BigDecimal amount) {
//        Payment payment = new Payment();
//        payment.setAmount(amount);
//        payment.setBookingId(bookingId);
        return paymentGateway.processPayment(bookingId, amount);
    }
}

