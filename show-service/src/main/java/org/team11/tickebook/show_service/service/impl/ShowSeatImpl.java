package org.team11.tickebook.show_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team11.tickebook.show_service.exception.ShowSeatDoesNotExistException;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.repository.ShowSeatRepository;
import org.team11.tickebook.show_service.service.ShowSeatService;
@RequiredArgsConstructor
@Service
public class ShowSeatImpl implements ShowSeatService {
    private final ShowSeatRepository showSeatRepository;
    @Override
    public ShowSeat bookSeat(ShowSeat showSeat) {
        if (showSeatRepository.existsById(showSeat.getId())){
            return showSeatRepository.save(showSeat);
        }else {
            throw new ShowSeatDoesNotExistException("Show seat does not exist with details: "+showSeat);
        }
    }
}
