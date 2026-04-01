package org.team11.tickebook.auth_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team11.tickebook.auth_service.kafka.UserCreatedEvent;
import org.team11.tickebook.auth_service.kafka.UserEventProducer;
import org.team11.tickebook.auth_service.model.entity.User;
import org.team11.tickebook.auth_service.model.enums.Role;
import org.team11.tickebook.auth_service.repository.UserRepository;
import org.team11.tickebook.auth_service.service.AdminSeeder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminSeederTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private AdminSeeder adminSeeder;

    // ---------- RUN ----------

    @Test
    void run_shouldCreateAdmin_andPublishEvent_whenAdminDoesNotExist() throws Exception {
        when(userRepository.existsByRolesContains(Role.ADMIN)).thenReturn(false);
        when(passwordEncoder.encode("admin123")).thenReturn("encoded-password");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        adminSeeder.run();

        // Verify user saved
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("admin@tickebook.com", savedUser.getEmail());
        assertEquals("encoded-password", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(Role.ADMIN));
        assertTrue(savedUser.isActive());

        // Verify event published
        verify(userEventProducer).publishUserCreated(any(UserCreatedEvent.class));
    }

    @Test
    void run_shouldNotCreateAdmin_whenAdminAlreadyExists() throws Exception {
        when(userRepository.existsByRolesContains(Role.ADMIN)).thenReturn(true);

        adminSeeder.run();

        verify(userRepository, never()).save(any());
        verify(userEventProducer, never()).publishUserCreated(any());
    }

    @Test
    void run_shouldEncodePassword_beforeSaving() throws Exception {
        when(userRepository.existsByRolesContains(Role.ADMIN)).thenReturn(false);
        when(passwordEncoder.encode("admin123")).thenReturn("secure-pass");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        adminSeeder.run();

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("secure-pass", savedUser.getPassword());
    }

    @Test
    void run_shouldAssignAdminRole_only() throws Exception {
        when(userRepository.existsByRolesContains(Role.ADMIN)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        adminSeeder.run();

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(1, savedUser.getRoles().size());
        assertTrue(savedUser.getRoles().contains(Role.ADMIN));
    }

    @Test
    void run_shouldSetActiveTrue_forAdmin() throws Exception {
        when(userRepository.existsByRolesContains(Role.ADMIN)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        adminSeeder.run();

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertTrue(savedUser.isActive());
    }
}