package com.cinema.reservation.controller;

import com.cinema.reservation.model.entity.Place;
import com.cinema.reservation.model.entity.Reservation;
import com.cinema.reservation.repository.HallRepository;
import com.cinema.reservation.repository.ReservationRepository;
import com.cinema.reservation.service.ReservationService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/places/{eventId}")
    List<Place> getEventPlaces(@PathVariable Long eventId) {
        return reservationService.findPlacesByEventId(eventId);
    }

    @PostMapping("/places") // transaction
    ResponseEntity<Long> bookPlaces(@RequestBody List<Place> places) {
        return ResponseEntity.ok(reservationService.bookPlaces(places));
    }

    @GetMapping("/reservations/{reservationId}")
    ResponseEntity<Reservation> getReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationRepository.findReservationById(reservationId));
    }

    @PostMapping("/reservations/{reservationId}/pay")
    ResponseEntity<Reservation> payReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.payReservation(reservationId));
    }

    @PostMapping("/reservations/{reservationId}/cancell")
    ResponseEntity<Reservation> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }
}
