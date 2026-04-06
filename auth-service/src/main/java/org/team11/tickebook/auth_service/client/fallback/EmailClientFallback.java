package org.team11.tickebook.auth_service.client.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.team11.tickebook.auth_service.client.EmailClient;
import org.team11.tickebook.auth_service.client.Mail;

@Component
public class EmailClientFallback implements EmailClient {

    @Override
    public ResponseEntity<String> sendMail(Mail mail) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Mail service unavailable");
    }
}