package org.team11.tickebook.payment_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team11.tickebook.payment_service.model.PaymentRequest;
import org.team11.tickebook.payment_service.model.PaymentResponse;
import org.team11.tickebook.payment_service.service.PaymentService;

@RestController
@RequestMapping("/internal/payments")
@RequiredArgsConstructor
public class InternalPaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse process(@RequestBody PaymentRequest request) {

        boolean success = paymentService.processPayment(
                request.bookingId(),
                request.amount()
        );

        return new PaymentResponse(success,
                success ? "Payment successful" : "Payment failed");
    }
}
