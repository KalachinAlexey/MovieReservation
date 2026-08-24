package com.cinema.reservation.controller;

import com.cinema.reservation.model.entity.Hall;
import com.cinema.reservation.repository.HallRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HallController {
    private final HallRepository hallRepository;

    public HallController(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @GetMapping("/halls")
    List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    @PostMapping("/halls")
    void postHall(@RequestBody Hall hall) {
        hallRepository.save(hall);
    }
}
