package org.team11.tickebook.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.team11.tickebook.adminservice.model.Role;

import java.util.UUID;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthClient {

    @PutMapping("/internal/user-role")
    ResponseEntity<String> updateUserRole(
            @RequestParam UUID userId,
            @RequestParam Role role
    );
}

