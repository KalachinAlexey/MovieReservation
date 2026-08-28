package com.cinema.reservation.errors.notfound;

public class EventNotFound extends RuntimeException {
    public EventNotFound(Long eventId) {
        super("Event id: " + eventId);
    }
}
