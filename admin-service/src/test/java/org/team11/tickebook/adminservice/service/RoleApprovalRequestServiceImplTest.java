package org.team11.tickebook.adminservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.adminservice.client.AuthClient;
import org.team11.tickebook.adminservice.dto.RoleApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.RoleApprovalResponseDto;
import org.team11.tickebook.adminservice.exception.RoleRequestNotFoundException;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.Role;
import org.team11.tickebook.adminservice.model.RoleElevationRequest;
import org.team11.tickebook.adminservice.repository.AdminProfileRepository;
import org.team11.tickebook.adminservice.repository.RoleElevationRequestRepository;
import org.team11.tickebook.adminservice.service.impl.RoleApprovalRequestServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleApprovalRequestServiceImplTest {

    @Mock
    private RoleElevationRequestRepository repository;

    @Mock
    private AdminProfileRepository adminProfileRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private RoleApprovalRequestServiceImpl service;

    // -------- CREATE REQUEST --------

    @Test
    void createRequest_shouldSaveRequest() {

        RoleApprovalRequestDto dto = new RoleApprovalRequestDto();
        dto.setRequestedBy(UUID.randomUUID());
        dto.setCurrentRole(Role.valueOf("USER"));
        dto.setRequestedRole(Role.valueOf("ADMIN"));
        dto.setDescription("Need admin access");

        Boolean result = service.createRequest(dto);

        assertTrue(result);
        verify(repository).save(any(RoleElevationRequest.class));
    }

    // -------- CHECK STATUS --------

    @Test
    void checkStatus_shouldReturnRequests() {

        UUID userId = UUID.randomUUID();

        RoleElevationRequest request = new RoleElevationRequest();
        request.setRequestedBy(userId);

        when(repository.findByRequestedBy(userId))
                .thenReturn(List.of(request));

        List<RoleApprovalResponseDto> result = service.checkStatus(userId);

        assertEquals(1, result.size());
        verify(repository).findByRequestedBy(userId);
    }

    // -------- REVIEW REQUEST APPROVED --------

    @Test
    void reviewRequest_shouldApproveAndUpdateRole() {

        UUID id = UUID.randomUUID();
        UUID reviewer = UUID.randomUUID();

        RoleElevationRequest request = new RoleElevationRequest();
        request.setId(id);
        request.setRequestedBy(UUID.randomUUID());
        request.setRequestedRole(Role.valueOf("ADMIN"));

        when(repository.findById(id)).thenReturn(Optional.of(request));
        when(repository.save(any(RoleElevationRequest.class))).thenReturn(request);

        RoleApprovalResponseDto result = service.reviewRequest(
                id,
                ApprovalStatus.APPROVED,
                "Approved",
                reviewer
        );

        assertEquals(ApprovalStatus.APPROVED, result.getStatus());

        verify(authClient).updateUserRole(
                request.getRequestedBy(),
                request.getRequestedRole()
        );

        verify(repository).save(request);
    }

    // -------- REVIEW REQUEST REJECTED --------

    @Test
    void reviewRequest_shouldRejectWithoutUpdatingRole() {

        UUID id = UUID.randomUUID();
        UUID reviewer = UUID.randomUUID();

        RoleElevationRequest request = new RoleElevationRequest();
        request.setId(id);
        request.setRequestedBy(UUID.randomUUID());

        when(repository.findById(id)).thenReturn(Optional.of(request));
        when(repository.save(any(RoleElevationRequest.class))).thenReturn(request);

        RoleApprovalResponseDto result = service.reviewRequest(
                id,
                ApprovalStatus.REJECTED,
                "Rejected",
                reviewer
        );

        assertEquals(ApprovalStatus.REJECTED, result.getStatus());

        verify(authClient, never()).updateUserRole(any(), any());
        verify(repository).save(request);
    }

    // -------- REVIEW REQUEST NOT FOUND --------

    @Test
    void reviewRequest_shouldThrowException_whenNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                service.reviewRequest(id, ApprovalStatus.APPROVED, "remarks", UUID.randomUUID())
        );
    }

    // -------- GET BY ID SUCCESS --------

    @Test
    void getById_shouldReturnRequest() {

        UUID id = UUID.randomUUID();

        RoleElevationRequest request = new RoleElevationRequest();
        request.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(request));

        RoleApprovalResponseDto result = service.getById(id);

        assertNotNull(result);
        verify(repository).findById(id);
    }

    // -------- GET BY ID NOT FOUND --------

    @Test
    void getById_shouldThrowException_whenNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RoleRequestNotFoundException.class,
                () -> service.getById(id));
    }

    // -------- GET ALL --------

    @Test
    void getAll_shouldReturnAllRequests() {

        when(repository.findAll())
                .thenReturn(List.of(new RoleElevationRequest(), new RoleElevationRequest()));

        List<RoleElevationRequest> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }
}