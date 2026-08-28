package com.cinema.reservation.errors;

public class PlaceValidationException extends RuntimeException {
    public PlaceValidationException(Long placeId) {
        super("Place with id " + placeId + " is not valid");
    }
}
