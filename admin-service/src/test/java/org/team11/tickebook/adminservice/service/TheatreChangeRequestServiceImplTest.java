package org.team11.tickebook.adminservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.adminservice.dto.TheatreChangeRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreChangeResponseDto;
import org.team11.tickebook.adminservice.exception.AdminProfileNotFoundException;
import org.team11.tickebook.adminservice.exception.TheatreChangeRequestNotFoundException;
import org.team11.tickebook.adminservice.model.AdminProfile;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.ChangeType;
import org.team11.tickebook.adminservice.model.TheatreChangeRequest;
import org.team11.tickebook.adminservice.repository.AdminProfileRepository;
import org.team11.tickebook.adminservice.repository.TheatreChangeRequestRepository;
import org.team11.tickebook.adminservice.service.impl.TheatreChangeRequestServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TheatreChangeRequestServiceImplTest {

    @Mock
    private TheatreChangeRequestRepository repository;

    @Mock
    private AdminProfileRepository adminProfileRepository;

    @InjectMocks
    private TheatreChangeRequestServiceImpl service;

    // -------- CREATE CHANGE REQUEST --------

    @Test
    void createChangeRequest_shouldCreateRequest() {

        TheatreChangeRequestDto dto = new TheatreChangeRequestDto();
        dto.setTheatreId(UUID.randomUUID());
        dto.setChangeType(ChangeType.valueOf("ADDRESS"));
        dto.setOldValue("Old Address");
        dto.setNewValue("New Address");
        dto.setRequestedBy(UUID.randomUUID());

        TheatreChangeRequest saved = new TheatreChangeRequest();
        saved.setId(UUID.randomUUID());

        when(repository.save(any(TheatreChangeRequest.class))).thenReturn(saved);

        TheatreChangeResponseDto result = service.createChangeRequest(dto);

        assertNotNull(result);
        verify(repository).save(any(TheatreChangeRequest.class));
    }

    // -------- GET ALL REQUESTS --------

    @Test
    void getAllRequests_shouldReturnRequests() {

        when(repository.findAll())
                .thenReturn(List.of(new TheatreChangeRequest(), new TheatreChangeRequest()));

        List<TheatreChangeResponseDto> result = service.getAllRequests();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    // -------- APPROVE OR REJECT SUCCESS --------

    @Test
    void approveOrReject_shouldUpdateRequest() {

        UUID requestId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();

        TheatreChangeRequest request = new TheatreChangeRequest();
        request.setId(requestId);

        AdminProfile admin = new AdminProfile();
        admin.setId(adminId);

        when(repository.findById(requestId)).thenReturn(Optional.of(request));
        when(adminProfileRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(repository.save(any(TheatreChangeRequest.class))).thenReturn(request);

        TheatreChangeResponseDto result = service.approveOrReject(
                requestId,
                ApprovalStatus.APPROVED,
                adminId,
                "Approved"
        );

        assertEquals(ApprovalStatus.APPROVED, result.getStatus());
        verify(repository).save(request);
    }

    // -------- REQUEST NOT FOUND --------

    @Test
    void approveOrReject_shouldThrowException_whenRequestNotFound() {

        UUID requestId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();

        when(repository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(
                TheatreChangeRequestNotFoundException.class,
                () -> service.approveOrReject(requestId, ApprovalStatus.APPROVED, adminId, "remarks")
        );
    }

    // -------- ADMIN NOT FOUND --------

    @Test
    void approveOrReject_shouldThrowException_whenAdminNotFound() {

        UUID requestId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();

        TheatreChangeRequest request = new TheatreChangeRequest();
        request.setId(requestId);

        when(repository.findById(requestId)).thenReturn(Optional.of(request));
        when(adminProfileRepository.findById(adminId)).thenReturn(Optional.empty());

        assertThrows(
                AdminProfileNotFoundException.class,
                () -> service.approveOrReject(requestId, ApprovalStatus.APPROVED, adminId, "remarks")
        );
    }
}