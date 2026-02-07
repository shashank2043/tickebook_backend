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
public class AdminSeeder implements CommandLineRunner {

    private final AdminProfileRepository repo;

    @Override
    public void run(String... args) {

        UUID userId = UUID.fromString("a23b12fa-5105-4d3b-b2cf-22b55d765418");

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
