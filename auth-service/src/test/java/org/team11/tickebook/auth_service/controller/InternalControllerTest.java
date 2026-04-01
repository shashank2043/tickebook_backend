package org.team11.tickebook.auth_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.team11.tickebook.auth_service.model.enums.Role;
import org.team11.tickebook.auth_service.service.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InternalControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private InternalController controller;

    // ================= UPDATE USER ROLE =================

    @Test
    void updateUserRole_shouldReturnSuccessMessage_whenValidInput() {

        UUID userId = UUID.randomUUID();
        Role role = Role.ADMIN;

        ResponseEntity<String> response =
                controller.updateUserRole(userId, role);

        // ✅ Status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // ✅ Body
        assertEquals("User role updated successfully", response.getBody());

        // ✅ Verify service call
        verify(userService).updateUserRole(userId, role);
    }

    @Test
    void updateUserRole_shouldCallServiceOnce_whenInvoked() {

        UUID userId = UUID.randomUUID();
        Role role = Role.ADMIN;

        controller.updateUserRole(userId, role);

        verify(userService, times(1)).updateUserRole(userId, role);
    }

    @Test
    void updateUserRole_shouldPropagateException_whenServiceFails() {

        UUID userId = UUID.randomUUID();
        Role role = Role.ADMIN;

        doThrow(new RuntimeException("User not found"))
                .when(userService).updateUserRole(userId, role);

        assertThrows(RuntimeException.class,
                () -> controller.updateUserRole(userId, role));

        verify(userService).updateUserRole(userId, role);
    }

    @Test
    void updateUserRole_shouldHandleDifferentRoles() {

        UUID userId = UUID.randomUUID();

        ResponseEntity<String> response =
                controller.updateUserRole(userId, Role.USER);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).updateUserRole(userId, Role.USER);
    }
}
