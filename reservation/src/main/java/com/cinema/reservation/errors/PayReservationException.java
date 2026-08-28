package com.cinema.reservation.errors;

public class PayReservationException extends RuntimeException {
    public PayReservationException(Long reservationId) {
        super("Reservation with id: " + reservationId + " isn't waiting for payment");
    }
}
