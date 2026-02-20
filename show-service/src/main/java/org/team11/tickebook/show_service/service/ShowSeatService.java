package org.team11.tickebook.show_service.service;

import org.team11.tickebook.show_service.model.ShowSeat;

public interface ShowSeatService {

    ShowSeat lockSeat(ShowSeat showSeat);

    ShowSeat confirmSeat(ShowSeat showSeat);

    ShowSeat releaseSeat(ShowSeat showSeat);
}
