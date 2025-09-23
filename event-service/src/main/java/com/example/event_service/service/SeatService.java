package com.example.event_service.service;


public interface SeatService {
    boolean reserveSeat(long seatId, long userId);

    boolean bookSeat(long seatId, long userId);

    public boolean isSeatReserved(long seatId);
}
