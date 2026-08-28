package com.cinema.reservation.controller;

import com.cinema.reservation.errors.notfound.FilmNotFound;
import com.cinema.reservation.model.entity.Film;
import com.cinema.reservation.repository.FilmRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class FilmController {

    private final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping("/films")
    ResponseEntity<List<Film>> getAllFilms() {
        return ResponseEntity.ok(filmRepository.findAll());
    }

    @GetMapping("/films/{filmTitle}")
    ResponseEntity<Film> getFilmByTitle(@PathVariable String filmTitle) {
        return ResponseEntity.ok(filmRepository.findByTitle(filmTitle)
                .orElseThrow(() -> new FilmNotFound(filmTitle)));
    }

    @PostMapping("/films")
    ResponseEntity<Film> postFilm(@RequestBody Film film, UriComponentsBuilder ucb) { // fix response
        filmRepository.save(film);
        URI location = ucb.path("/films/{filmTitle}").buildAndExpand(film.getTitle()).toUri();
        return ResponseEntity.created(location).build();
    }
}
