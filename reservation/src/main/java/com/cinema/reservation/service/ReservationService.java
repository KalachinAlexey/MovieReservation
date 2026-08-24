package com.cinema.reservation.service;

import com.cinema.reservation.model.entity.FilmEvent;
import com.cinema.reservation.model.entity.Place;
import com.cinema.reservation.model.entity.Reservation;

import java.util.List;

public interface ReservationService {
    List<Place> findPlacesByEventId(long eventId);
    Long bookPlaces(List<Place> places, String username);
    void addPlacesForEvent(FilmEvent event);
    Reservation payReservation(Long reservationId, String username);
    Reservation cancelReservation(Long reservationId, String username);
}
