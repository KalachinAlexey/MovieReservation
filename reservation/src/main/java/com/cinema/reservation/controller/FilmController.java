package com.cinema.reservation.controller;

import com.cinema.reservation.model.entity.Film;
import com.cinema.reservation.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FilmController {

    private final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping("/films")
    List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @GetMapping("/films/{filmTitle}")
    Film getFilmByTitle(@PathVariable String filmTitle) {
        return filmRepository.findByTitle(filmTitle);
    }

    @PostMapping("/films")
    void postFilm(@RequestBody Film film) {
        filmRepository.save(film);
    }
}
