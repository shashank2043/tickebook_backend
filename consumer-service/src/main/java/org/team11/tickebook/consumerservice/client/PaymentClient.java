package org.team11.tickebook.consumerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.team11.tickebook.consumerservice.dto.PaymentRequest;
import org.team11.tickebook.consumerservice.dto.PaymentResponse;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/internal/payments")
    PaymentResponse processPayment(@RequestBody PaymentRequest request);
}
