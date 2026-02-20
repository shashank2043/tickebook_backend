package org.team11.tickebook.payment_service.gateway;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public boolean processPayment(UUID bookingId, BigDecimal amount) {

        try {
            Thread.sleep(2000); // simulate delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 80% success
        return Math.random() > 0.2;
    }
}
