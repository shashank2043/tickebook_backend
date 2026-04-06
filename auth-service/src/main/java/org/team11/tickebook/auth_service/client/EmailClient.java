package org.team11.tickebook.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.team11.tickebook.auth_service.client.fallback.EmailClientFallback;

@FeignClient(name = "MAIL-SERVER", path = "/api/mail-server",fallback = EmailClientFallback.class)
public interface EmailClient {
    @PostMapping("/send")
    ResponseEntity<String> sendMail(@RequestBody Mail mail);
}
