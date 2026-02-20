package org.team11.tickebook.payment_service.gateway;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentGateway {

    boolean processPayment(UUID bookingId, BigDecimal amount);
}
