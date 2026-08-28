package com.cinema.reservation.errors;

public class PlaceAlreadyBookedException extends RuntimeException {
    public PlaceAlreadyBookedException(Long placeId) {
        super("Place with id: " + placeId + " already booked");
    }
}
