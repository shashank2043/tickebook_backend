package org.team11.tickebook.adminservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team11.tickebook.adminservice.model.AdminProfile;
import org.team11.tickebook.adminservice.repository.AdminProfileRepository;
import org.team11.tickebook.adminservice.service.AdminProfileService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminProfileServiceImpl implements AdminProfileService {

    private final AdminProfileRepository repository;

    @Override
    public AdminProfile createAdmin(AdminProfile adminProfile) {
        adminProfile.setCreatedAt(LocalDateTime.now());
        adminProfile.setUpdatedAt(LocalDateTime.now());
        return repository.save(adminProfile);
    }

    @Override
    public AdminProfile updateAdmin(UUID id, AdminProfile updated) {
        AdminProfile existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin profile not found"));

        existing.setRegion(updated.getRegion());
        existing.setCanApproveRoles(updated.getCanApproveRoles());
        existing.setCanApproveTheaters(updated.getCanApproveTheaters());
        existing.setCanApproveChanges(updated.getCanApproveChanges());
        existing.setLastActionAt(updated.getLastActionAt());
        existing.setRemarks(updated.getRemarks());
        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }

    @Override
    public void deleteAdmin(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Admin profile not found");
        }
        repository.deleteById(id);
    }
}
