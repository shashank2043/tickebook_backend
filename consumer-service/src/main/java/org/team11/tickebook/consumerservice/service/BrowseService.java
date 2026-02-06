package org.team11.tickebook.consumerservice.service;

import org.team11.tickebook.consumerservice.dto.Show;

import java.util.List;

public interface BrowseService {
    List<Show> fetchAllShows();
}
