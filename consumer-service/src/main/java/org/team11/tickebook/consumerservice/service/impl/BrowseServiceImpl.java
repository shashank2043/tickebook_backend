package org.team11.tickebook.consumerservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.team11.tickebook.consumerservice.client.ShowClient;
import org.team11.tickebook.consumerservice.dto.Show;
import org.team11.tickebook.consumerservice.service.BrowseService;

import java.util.List;

public class BrowseServiceImpl implements BrowseService {
    @Autowired
    public ShowClient showClient;
    @Override
    public List<Show> fetchAllShows() {
        return showClient.getAllShows();
    }
}
