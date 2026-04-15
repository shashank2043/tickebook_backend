package org.team11.tickebook.consumerservice.client.fallback;

import org.springframework.stereotype.Component;
import org.team11.tickebook.consumerservice.client.PaymentClient;
import org.team11.tickebook.consumerservice.dto.PaymentRequest;
import org.team11.tickebook.consumerservice.dto.PaymentResponse;

@Component
public class PaymentClientFallback implements PaymentClient {

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        return new PaymentResponse(false, "Payment service is unavailable");
    }
}
