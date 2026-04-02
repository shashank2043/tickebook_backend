package org.team11.tickebook.adminservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.adminservice.exception.AdminProfileNotFoundException;
import org.team11.tickebook.adminservice.model.AdminProfile;
import org.team11.tickebook.adminservice.repository.AdminProfileRepository;
import org.team11.tickebook.adminservice.service.impl.AdminProfileServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminProfileServiceImplTest {

    @Mock
    private AdminProfileRepository repository;

    @InjectMocks
    private AdminProfileServiceImpl service;

    // -------- CREATE ADMIN --------

    @Test
    void createAdmin_shouldSaveAdmin() {

        AdminProfile admin = new AdminProfile();
        admin.setRegion("US");

        when(repository.save(any(AdminProfile.class))).thenReturn(admin);

        AdminProfile result = service.createAdmin(admin);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(repository).save(admin);
    }

    // -------- UPDATE ADMIN SUCCESS --------

    @Test
    void updateAdmin_shouldUpdateAdmin() {

        UUID id = UUID.randomUUID();

        AdminProfile existing = new AdminProfile();
        existing.setId(id);
        existing.setRegion("Old");

        AdminProfile updated = new AdminProfile();
        updated.setRegion("New");
        updated.setRemarks("Updated remarks");
        updated.setLastActionAt(LocalDateTime.now());

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(AdminProfile.class))).thenReturn(existing);

        AdminProfile result = service.updateAdmin(id, updated);

        assertEquals("New", result.getRegion());
        assertEquals("Updated remarks", result.getRemarks());

        verify(repository).findById(id);
        verify(repository).save(existing);
    }

    // -------- UPDATE ADMIN NOT FOUND --------

    @Test
    void updateAdmin_shouldThrowException_whenAdminNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                AdminProfileNotFoundException.class,
                () -> service.updateAdmin(id, new AdminProfile())
        );

        verify(repository).findById(id);
    }

    // -------- DELETE ADMIN SUCCESS --------

    @Test
    void deleteAdmin_shouldDeleteAdmin() {

        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(true);

        service.deleteAdmin(id);

        verify(repository).deleteById(id);
    }

    // -------- DELETE ADMIN NOT FOUND --------

    @Test
    void deleteAdmin_shouldThrowException_whenAdminNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(
                AdminProfileNotFoundException.class,
                () -> service.deleteAdmin(id)
        );

        verify(repository).existsById(id);
    }
}