package com.cinema.reservation.controller;

import com.cinema.reservation.model.entity.Film;
import com.cinema.reservation.model.entity.FilmEvent;
import com.cinema.reservation.repository.EventRepository;
import com.cinema.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class EventController {
    private final EventRepository eventRepository;
    private final ReservationService reservationService;

    public EventController(EventRepository eventRepository, ReservationService reservationService) {
        this.eventRepository = eventRepository;
        this.reservationService = reservationService;
    }

    @GetMapping("/events")
    ResponseEntity<List<FilmEvent>> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    @PostMapping("/events")
    @Transactional
    ResponseEntity<Void> postEvent(@RequestBody FilmEvent event) {
        eventRepository.save(event);
        reservationService.addPlacesForEvent(event);
        return ResponseEntity.ok().build();
    }
}
