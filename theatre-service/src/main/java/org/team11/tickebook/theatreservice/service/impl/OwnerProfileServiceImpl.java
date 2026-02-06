package org.team11.tickebook.theatreservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team11.tickebook.theatreservice.clients.AdminClient;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;
import org.team11.tickebook.theatreservice.exception.OwnerProfileNotFoundException;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;
import org.team11.tickebook.theatreservice.repository.TheatreOwnerProfileRepository;
import org.team11.tickebook.theatreservice.service.OwnerProfileService;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OwnerProfileServiceImpl implements OwnerProfileService {

    private final TheatreOwnerProfileRepository profileRepository;
    private final AdminClient adminClient;

    @Override
    public TheatreOwnerProfile create(TheatreOwnerProfile profile) {
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        return profileRepository.save(profile);
    }

    @Override
    public TheatreOwnerProfile get(UUID id) {
        return profileRepository.findById(id)
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
    public TheatreApprovalResponseDto requestTheatreApproval(TheatreApprovalRequestDto request) {
        TheatreOwnerProfile theatreOwnerProfile = profileRepository.findById(request.getTheatreOwnerProfileId()).orElseThrow(() -> new OwnerProfileNotFoundException("Owner profile not found with id " + request.getTheatreOwnerProfileId()));
        boolean theatreExists = theatreOwnerProfile.getTheatres()
                .stream()
                .anyMatch(t -> t.getId().equals(request.getTheatreId()));
        if (!theatreExists) {
            throw new IllegalArgumentException(
                    "Theatre " + request.getTheatreId() +
                            " does not belong to owner " + request.getTheatreOwnerProfileId()
            );
        }
        return adminClient.createRequest(request).getBody();
    }
}
