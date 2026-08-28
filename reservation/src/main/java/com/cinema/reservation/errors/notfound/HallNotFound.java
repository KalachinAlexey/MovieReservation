package com.cinema.reservation.errors.notfound;

public class HallNotFound extends RuntimeException {
    public HallNotFound(Long hallId) {
        super("Hall id: " + hallId);
    }
}
