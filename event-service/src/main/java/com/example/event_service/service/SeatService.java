package com.example.event_service.service;


import com.example.event_service.dto.SeatResponseDTO;

import java.util.List;

public interface SeatService {
    boolean reserveSeat(long seatId, long userId);

    boolean bookSeat(long seatId, long userId);

    boolean isSeatReserved(long seatId);

    List<SeatResponseDTO> getSeatsByEventId(long eventId);
}
