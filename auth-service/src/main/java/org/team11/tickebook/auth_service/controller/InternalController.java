package org.team11.tickebook.auth_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.auth_service.model.enums.Role;
import org.team11.tickebook.auth_service.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {

    private final UserService userService;

    @PutMapping("/user-role")
    public ResponseEntity<String> updateUserRole(
            @RequestParam UUID userId,
            @RequestParam Role role
    ) {
        userService.updateUserRole(userId, role);
        return ResponseEntity.ok("User role updated successfully");
    }
}
