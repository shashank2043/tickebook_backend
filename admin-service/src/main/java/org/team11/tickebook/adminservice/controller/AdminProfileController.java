package org.team11.tickebook.adminservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.adminservice.model.AdminProfile;
import org.team11.tickebook.adminservice.service.AdminProfileService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminProfileController {

    private final AdminProfileService adminProfileService;

    @PostMapping
    public ResponseEntity<AdminProfile> createAdmin(@RequestBody AdminProfile adminProfile) {
        return ResponseEntity.ok(adminProfileService.createAdmin(adminProfile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminProfile> updateAdmin(
            @PathVariable UUID id,
            @RequestBody AdminProfile adminProfile) {

        return ResponseEntity.ok(adminProfileService.updateAdmin(id, adminProfile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
        adminProfileService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
