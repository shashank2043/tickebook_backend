package org.team11.tickebook.adminservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.team11.tickebook.adminservice.model.AdminProfile;
import org.team11.tickebook.adminservice.repository.AdminProfileRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminSeeder {

    private final AdminProfileRepository repo;

    public void createAdmin(UUID userId) {

        if (repo.findByUserId(userId).isEmpty()) {

            AdminProfile admin = new AdminProfile();
            admin.setUserId(userId);
            admin.setRegion("GLOBAL");
            admin.setCanApproveRoles(true);
            admin.setCanApproveTheaters(true);
            admin.setCanApproveChanges(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            repo.save(admin);
        }
    }
}
