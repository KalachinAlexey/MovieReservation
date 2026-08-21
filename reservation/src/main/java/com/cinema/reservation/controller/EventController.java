package com.cinema.reservation.controller;

import com.cinema.reservation.model.entity.Film;
import com.cinema.reservation.model.entity.FilmEvent;
import com.cinema.reservation.repository.EventRepository;
import com.cinema.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {
    private EventRepository eventRepository;
    private ReservationService reservationService;

    public EventController(EventRepository eventRepository, ReservationService reservationService) {
        this.eventRepository = eventRepository;
        this.reservationService = reservationService;
    }

    @GetMapping("/events")
    List<FilmEvent> getAllEvents() {
        return eventRepository.findAll();
    }

    @PostMapping("/events") // transaction
    void postEvent(@RequestBody FilmEvent event) {
        eventRepository.save(event);
        reservationService.addPlacesForEvent(event);
    }
}
