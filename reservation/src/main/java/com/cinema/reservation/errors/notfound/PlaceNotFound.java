package com.cinema.reservation.errors.notfound;

public class PlaceNotFound extends EntityElementNotFound {
    public PlaceNotFound(Long placeId) {
        super("Place id: " + placeId);
    }
}
