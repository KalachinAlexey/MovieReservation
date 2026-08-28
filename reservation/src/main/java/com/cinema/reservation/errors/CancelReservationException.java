package com.cinema.reservation.errors;

public class CancelReservationException extends RuntimeException {
    public CancelReservationException(Long reservationId) {
        super("You can't cancel reservation with number: " + reservationId);
    }
}
