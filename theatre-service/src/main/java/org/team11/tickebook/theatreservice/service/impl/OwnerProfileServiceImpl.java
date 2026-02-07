package org.team11.tickebook.theatreservice.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.team11.tickebook.theatreservice.clients.AdminClient;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;
import org.team11.tickebook.theatreservice.exception.OwnerProfileNotFoundException;
import org.team11.tickebook.theatreservice.model.ApprovalStatus;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;
import org.team11.tickebook.theatreservice.repository.TheatreOwnerProfileRepository;
import org.team11.tickebook.theatreservice.repository.TheatreRepository;
import org.team11.tickebook.theatreservice.service.OwnerProfileService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OwnerProfileServiceImpl implements OwnerProfileService {

    private final TheatreOwnerProfileRepository profileRepository;
    private final AdminClient adminClient;
    private final TheatreRepository theatreRepository;

    @Override
    public TheatreOwnerProfile create(TheatreOwnerProfile profile) {
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        profile.setIsVerified(false);
        return profileRepository.save(profile);
    }

    @Override
    public TheatreOwnerProfile getByUserId(UUID id) {
        return profileRepository.findByUserId(id)
                .orElseThrow(() -> new OwnerProfileNotFoundException("Owner not found with id "+id));
    }

    @Override
    public TheatreOwnerProfile update(TheatreOwnerProfile profile) {
        if(profileRepository.existsById(profile.getId())){
            return profileRepository.save(profile);
        }
        else throw new OwnerProfileNotFoundException("Owner Profile not found");
    }

    @Override
    @Transactional
    public Boolean requestTheatreApproval(
            Authentication authentication,
            TheatreApprovalRequestDto request
    ) {

        Claims claims = (Claims) authentication.getPrincipal();
        UUID userId = UUID.fromString(claims.get("userId", String.class));

        TheatreOwnerProfile ownerProfile =
                profileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new OwnerProfileNotFoundException(
                                        "Owner profile not found for user " + userId));

        boolean theatreExists =
                theatreRepository.existsByIdAndOwnerProfile(
                        request.getTheatreId(),
                        ownerProfile
                );

        if (!theatreExists) {
            throw new IllegalArgumentException(
                    "Theatre does not belong to this owner");
        }

        TheatreApprovalRequestDto adminDto =
                TheatreApprovalRequestDto.builder()
                        .theatreId(request.getTheatreId())
                        .theatreOwnerProfileId(ownerProfile.getId())
                        .status(ApprovalStatus.PENDING)
                        .remarks(request.getRemarks())
                        .build();

        ResponseEntity<Boolean> response =
                adminClient.createRequest(adminDto);

        return Boolean.TRUE.equals(response.getBody());
    }


    @Override
    public List<TheatreApprovalResponseDto> checkStatus(
            UUID theatreId,
            Authentication authentication
    ) {

        Claims claims = (Claims) authentication.getPrincipal();
        UUID userId = UUID.fromString(claims.get("userId", String.class));

        TheatreOwnerProfile ownerProfile =
                profileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new OwnerProfileNotFoundException(
                                        "Owner profile not found"));

        boolean theatreExists =
                theatreRepository.existsByIdAndOwnerProfile(
                        theatreId,
                        ownerProfile
                );

        if (!theatreExists) {
            throw new IllegalArgumentException(
                    "Theatre does not belong to this owner");
        }

        ResponseEntity<List<TheatreApprovalResponseDto>> response =
                adminClient.checkStatus(theatreId);

        return response.getBody();
    }


    @Override
    @Transactional
    public void verifyOwner(UUID ownerProfileId) {

        TheatreOwnerProfile profile =
                profileRepository.findById(ownerProfileId)
                        .orElseThrow(() ->
                                new OwnerProfileNotFoundException("Owner not found"));

        profile.setIsVerified(true);
        profile.setUpdatedAt(LocalDateTime.now());

        profileRepository.save(profile);
    }
}
