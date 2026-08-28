package com.cinema.reservation.controller;

import com.cinema.reservation.annotations.CurrentOwner;
import com.cinema.reservation.errors.notfound.ReservationNotFound;
import com.cinema.reservation.model.dto.PlaceDto;
import com.cinema.reservation.model.entity.Place;
import com.cinema.reservation.model.entity.Reservation;
import com.cinema.reservation.repository.ReservationRepository;
import com.cinema.reservation.service.ReservationService;
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
    ResponseEntity<List<Place>> getEventPlaces(@PathVariable Long eventId) {
        return ResponseEntity.ok(reservationService.findPlacesByEventId(eventId));
    }

    @PostMapping("/places")
    ResponseEntity<Reservation> bookPlaces(@RequestBody List<PlaceDto> places, @CurrentOwner String owner) {
        return ResponseEntity.ok(reservationService.bookPlaces(places, owner));
    }

    @GetMapping("/reservations")
    ResponseEntity<List<Reservation>> getUserReservations(@CurrentOwner String owner) {
        return ResponseEntity.ok(reservationRepository.findReservationsByUsername(owner));
    }

    @GetMapping("/reservations/{reservationId}")
    ResponseEntity<Reservation> getReservation(@PathVariable Long reservationId, @CurrentOwner String owner) {
        return ResponseEntity.ok(reservationRepository.findReservationByIdAndUsername(reservationId, owner)
                .orElseThrow(() -> new ReservationNotFound(reservationId)));
    }

    @PostMapping("/reservations/{reservationId}/pay")
    ResponseEntity<Reservation> payReservation(@PathVariable Long reservationId, @CurrentOwner String owner) {
        return ResponseEntity.ok(reservationService.payReservation(reservationId, owner));
    }

    @PostMapping("/reservations/{reservationId}/cancell")
    ResponseEntity<Reservation> cancelReservation(@PathVariable Long reservationId, @CurrentOwner String owner) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId, owner));
    }
}
