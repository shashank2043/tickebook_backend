package org.team11.tickebook.adminservice.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.team11.tickebook.adminservice.client.TheatreClient;
import org.team11.tickebook.adminservice.dto.TheatreApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalResponseDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalReviewDto;
import org.team11.tickebook.adminservice.exception.TheatreApprovalRequestNotFoundException;
import org.team11.tickebook.adminservice.model.AdminProfile;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.TheatreApprovalRequest;
import org.team11.tickebook.adminservice.repository.AdminProfileRepository;
import org.team11.tickebook.adminservice.repository.TheatreApprovalRequestRepository;
import org.team11.tickebook.adminservice.security.SecurityUtil;
import org.team11.tickebook.adminservice.service.impl.TheatreApprovalRequestServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TheatreApprovalRequestServiceImplTest {

    @Mock
    private TheatreApprovalRequestRepository repository;

    @Mock
    private AdminProfileRepository adminProfileRepository;

    @Mock
    private TheatreClient theatreClient;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private TheatreApprovalRequestServiceImpl service;

    // -------- CREATE REQUEST --------

    @Test
    void createRequest_shouldSaveRequest() {

        TheatreApprovalRequestDto dto = new TheatreApprovalRequestDto();
        dto.setTheatreId(UUID.randomUUID());
        dto.setTheatreOwnerProfileId(UUID.randomUUID());

        Boolean result = service.createRequest(dto);

        assertTrue(result);
        verify(repository).save(any(TheatreApprovalRequest.class));
    }

    // -------- REVIEW REQUEST APPROVED --------

    @Test
    void reviewRequest_shouldApproveRequest() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        TheatreApprovalRequest request = new TheatreApprovalRequest();
        request.setId(id);
        request.setTheaterOwnerProfileId(UUID.randomUUID());

        TheatreApprovalReviewDto dto = new TheatreApprovalReviewDto();
        dto.setStatus(ApprovalStatus.APPROVED);
        dto.setRemarks("Approved");

        Claims claims = mock(Claims.class);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(claims);

        AdminProfile admin = new AdminProfile();
        admin.setId(UUID.randomUUID());

        when(repository.findById(id)).thenReturn(Optional.of(request));
        when(adminProfileRepository.findByUserId(userId)).thenReturn(Optional.of(admin));
        when(repository.save(any())).thenReturn(request);

        TheatreApprovalResponseDto result =
                service.reviewRequest(id, dto, authentication);

        assertEquals(ApprovalStatus.APPROVED, result.getStatus());

        verify(theatreClient).verifyOwner(request.getTheaterOwnerProfileId());
        verify(repository).save(request);
    }

    // -------- REVIEW REQUEST REJECTED --------

    @Test
    void reviewRequest_shouldRejectRequest() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        TheatreApprovalRequest request = new TheatreApprovalRequest();
        request.setId(id);

        TheatreApprovalReviewDto dto = new TheatreApprovalReviewDto();
        dto.setStatus(ApprovalStatus.REJECTED);

        Claims claims = mock(Claims.class);
        when(claims.get("userId", String.class)).thenReturn(userId.toString());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(claims);

        AdminProfile admin = new AdminProfile();
        admin.setId(UUID.randomUUID());

        when(repository.findById(id)).thenReturn(Optional.of(request));
        when(adminProfileRepository.findByUserId(userId)).thenReturn(Optional.of(admin));
        when(repository.save(any())).thenReturn(request);

        TheatreApprovalResponseDto result =
                service.reviewRequest(id, dto, authentication);

        assertEquals(ApprovalStatus.REJECTED, result.getStatus());

        verify(theatreClient, never()).verifyOwner(any());
    }

    // -------- REVIEW REQUEST NOT FOUND --------

    @Test
    void reviewRequest_shouldThrowException_whenRequestNotFound() {

        UUID id = UUID.randomUUID();

        TheatreApprovalReviewDto dto = new TheatreApprovalReviewDto();

        Authentication authentication = mock(Authentication.class);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                TheatreApprovalRequestNotFoundException.class,
                () -> service.reviewRequest(id, dto, authentication)
        );
    }

    // -------- GET BY ID --------

    @Test
    void getById_shouldReturnRequest() {

        UUID id = UUID.randomUUID();

        TheatreApprovalRequest request = new TheatreApprovalRequest();
        request.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(request));

        TheatreApprovalResponseDto result = service.getById(id);

        assertNotNull(result);
        verify(repository).findById(id);
    }

    // -------- GET BY ID NOT FOUND --------

    @Test
    void getById_shouldThrowException_whenNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                TheatreApprovalRequestNotFoundException.class,
                () -> service.getById(id)
        );
    }

    // -------- CHECK STATUS --------

    @Test
    void checkStatus_shouldReturnRequests() {

        UUID theatreId = UUID.randomUUID();

        when(repository.findByTheaterId(theatreId))
                .thenReturn(List.of(new TheatreApprovalRequest()));

        List<TheatreApprovalResponseDto> result =
                service.checkStatus(theatreId);

        assertEquals(1, result.size());
        verify(repository).findByTheaterId(theatreId);
    }

    // -------- GET ALL --------

    @Test
    void getAll_shouldReturnAllRequests() {

        when(repository.findAll())
                .thenReturn(List.of(new TheatreApprovalRequest(), new TheatreApprovalRequest()));

        List<TheatreApprovalResponseDto> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }
}