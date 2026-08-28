package com.cinema.reservation.errors.notfound;

public class EntityElementNotFound extends RuntimeException {
    public EntityElementNotFound(String info) {
        super("Element not found: " + info);
    }
}
