package com.cinema.reservation.errors.notfound;

public class ReservationNotFound extends EntityElementNotFound {
    public ReservationNotFound(Long reservationId) {
        super("Reservation id: " + reservationId);
    }
}
