//package org.team11.tickebook.adminservice.service.impl;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.team11.tickebook.adminservice.dto.RoleApprovalRequestDto;
//import org.team11.tickebook.adminservice.dto.RoleApprovalResponseDto;
//import org.team11.tickebook.adminservice.model.AdminProfile;
//import org.team11.tickebook.adminservice.model.ApprovalStatus;
//import org.team11.tickebook.adminservice.model.Role;
//import org.team11.tickebook.adminservice.model.RoleElevationRequest;
//import org.team11.tickebook.adminservice.repository.AdminProfileRepository;
//import org.team11.tickebook.adminservice.repository.RoleElevationRequestRepository;
//import org.team11.tickebook.adminservice.service.RoleApprovalRequestService;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class RoleApprovalRequestServiceImpl
//        implements RoleApprovalRequestService {
//
//    @Autowired
//    private RoleElevationRequestRepository repository;
//    @Autowired
//    private  AdminProfileRepository adminProfileRepository;
//
//    @Override
//    public RoleApprovalResponseDto createRequest(RoleApprovalRequestDto dto) {
//
//        AdminProfile admin = adminProfileRepository.findById(dto.getRequestedBy())
//                .orElseThrow(() -> new RuntimeException("Admin not found"));
//
//        RoleElevationRequest request = new RoleElevationRequest();
//        request.setRequestedBy(admin.getId());
//        request.setCurrentRole(admin.getRole());
//        request.setRequestedRole(dto.getRequestedRole());
//        request.setStatus(ApprovalStatus.PENDING);
//        request.setCreatedAt(LocalDateTime.now());
//
//        return mapToResponse(repository.save(request));
//    }
//
//    @Override
//    public RoleApprovalResponseDto reviewRequest(UUID id,
//                                                 ApprovalStatus status,
//                                                 String remarks) {
//
//        RoleElevationRequest request = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Request not found"));
//
//        request.setStatus(status);
//        request.setRemarks(remarks);
//        request.setReviewedAt(LocalDateTime.now());
//
//        AdminProfile reviewer = getCurrentAdmin(); // SUPER_ADMIN
//        request.setReviewedBy(reviewer);
//
//        // If approved → update admin role
//        if (status == ApprovalStatus.APPROVED) {
//            AdminProfile admin = adminProfileRepository
//                    .findById(request.getRequestedBy())
//                    .orElseThrow();
//            admin.setRole(request.getRequestedRole());
//            adminProfileRepository.save(admin);
//        }
//
//        return mapToResponse(repository.save(request));
//    }
//
//    @Override
//    public RoleApprovalResponseDto getById(UUID id) {
//        return repository.findById(id)
//                .map(this::mapToResponse)
//                .orElseThrow(() -> new RuntimeException("Request not found"));
//    }
//
//    // ---------------- MAPPER ----------------
//
//    private RoleApprovalResponseDto mapToResponse(RoleElevationRequest entity) {
//
//        RoleApprovalResponseDto dto = new RoleApprovalResponseDto();
//        dto.setId(entity.getId());
//        dto.setRequestedBy(entity.getRequestedBy());
//        dto.setCurrentRole(entity.getCurrentRole());
//        dto.setRequestedRole(entity.getRequestedRole());
//        dto.setStatus(entity.getStatus());
//        dto.setRemarks(entity.getRemarks());
//        dto.setReviewedAt(entity.getReviewedAt());
//        dto.setCreatedAt(entity.getCreatedAt());
//
//        if (entity.getReviewedBy() != null) {
//            dto.setReviewedByAdminId(entity.getReviewedBy().getId());
//        }
//
//        return dto;
//    }
//
//    // Mock – replace with Spring Security
//    private AdminProfile getCurrentAdmin() {
//        return adminProfileRepository.findFirstByCanApproveRolesTrue() // I replaced Super_admin into Admin
//                .orElseThrow();
//    }
//    }