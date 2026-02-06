package org.team11.tickebook.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MAIL-SERVER", path = "/api/mail-server")
public interface EmailClient {
    @PostMapping("/send")
    ResponseEntity<String> sendMail(@RequestBody Mail mail);
}
