package org.team11.tickebook.theatreservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team11.tickebook.theatreservice.model.Screen;
import org.team11.tickebook.theatreservice.repository.ScreenRepository;
import org.team11.tickebook.theatreservice.service.ScreenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {
    private final ScreenRepository repo;
    @Override
    public Screen create(Screen screen) {
        screen.setCreatedAt(LocalDateTime.now());
        screen.setUpdatedAt(LocalDateTime.now());
        screen.setIsActive(true);
        return repo.save(screen);
    }

    @Override
    public List<Screen> getByTheatre(UUID theatreId) {
        return repo.findByTheatreId(theatreId);
    }
}
