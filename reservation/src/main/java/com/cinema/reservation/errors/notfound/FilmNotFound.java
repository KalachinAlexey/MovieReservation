package com.cinema.reservation.errors.notfound;

public class FilmNotFound extends RuntimeException {
    public FilmNotFound(String title) {
        super("Film id: " + title);
    }
}
