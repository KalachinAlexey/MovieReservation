package com.cinema.reservation.controller;

import com.cinema.reservation.errors.notfound.HallNotFound;
import com.cinema.reservation.model.entity.Hall;
import com.cinema.reservation.repository.HallRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class HallController {
    private final HallRepository hallRepository;

    public HallController(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @GetMapping("/halls")
    ResponseEntity<List<Hall>> getAllHalls() {
        return ResponseEntity.ok(hallRepository.findAll());
    }

    @GetMapping("/halls/{hallId}")
    ResponseEntity<Hall> getHallById(@PathVariable Long hallId) {
        return ResponseEntity.ok(hallRepository.findHallById(hallId)
                .orElseThrow(() -> new HallNotFound(hallId)));
    }

    @PostMapping("/halls")
    ResponseEntity<Hall> postHall(@RequestBody Hall hall, UriComponentsBuilder ucb) {
        hallRepository.save(hall);
        URI location = ucb.path("/halls/{hallId}").buildAndExpand(hall.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
