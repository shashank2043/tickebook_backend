package org.team11.tickebook.theatreservice.service;

import org.team11.tickebook.theatreservice.model.Screen;

import java.util.List;
import java.util.UUID;

public interface ScreenService {
    Screen create(Screen screen);
    List<Screen> getByTheatre(UUID theatreId);
}
