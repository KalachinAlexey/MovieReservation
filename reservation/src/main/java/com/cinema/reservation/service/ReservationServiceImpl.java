package com.cinema.reservation.service;

import com.cinema.reservation.model.entity.*;
import com.cinema.reservation.repository.EventRepository;
import com.cinema.reservation.repository.HallRepository;
import com.cinema.reservation.repository.PlaceRepository;
import com.cinema.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ReservationServiceImpl implements ReservationService {
    private PlaceRepository placeRepository;
    private final HallRepository hallRepository;
    private final EventRepository eventRepository;
    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(PlaceRepository placeRepository,
                                  HallRepository hallRepository,
                                  EventRepository eventRepository, ReservationRepository reservationRepository) {
        this.placeRepository = placeRepository;
        this.hallRepository = hallRepository;
        this.eventRepository = eventRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Place> findPlacesByEventId(long eventId) {
        return placeRepository.findPlacesByFilmEventId(eventId);
    }

    @Override
    @Transactional
    public Long bookPlaces(List<Place> places, String username) {
        Reservation reservation = reservationRepository.save(new Reservation(username));
        places.forEach(place -> {
                Hall hall = hallRepository.findHallById(
                        eventRepository.findFilmEventById(place.getFilmEventId()).getHallId()
                );
                if (!validatePlace(place, hall)) {
                    throw new RuntimeException();
                }
//                Place repositoryPlace = placeRepository.findPlacesByFilmEventIdAndColumnAndRow(
//                        place.getFilmEventId(),
//                        place.getColumn(),
//                        place.getRow()
//                );
                placeRepository.bookPlace(place.getId(), reservation.getId());
                reservationRepository.addReservationPrice(place.getPrice(), reservation.getId());

//                    repositoryPlace.setReservationId(reservation.getId());
//                    repositoryPlace.setStatus(PlaceStatus.BOOKED);
//                    placeRepository.save(repositoryPlace);
        });
        return reservation.getId();
    }

    @Override
    @Transactional
    public void addPlacesForEvent(FilmEvent event) {
        Hall hall = hallRepository.findHallById(event.getHallId());
        for (int i = 1; i <= hall.getRows(); i++) {
            for (int j = 1; j <= hall.getColumns(); j++) {
                placeRepository.save(new Place(event.getId(), i, j));
            }
        }
    }

    @Override
    @Transactional
    public Reservation payReservation(Long reservationId, String username) {
        reservationRepository.payReservationById(reservationId, username);
        return reservationRepository.findReservationById(reservationId);
    }

    @Override
    @Transactional
    public Reservation cancelReservation(Long reservationId, String username) {
        reservationRepository.cancelReservationById(reservationId, username);
        placeRepository.unbookPlacesByReservation(reservationId);
        return reservationRepository.findReservationById(reservationId);
    }

    private boolean validatePlace(Place place, Hall hall) {
        return 1 <= place.getColumn() && place.getColumn() <= hall.getColumns() &&
                1 <= place.getRow() && place.getRow() <= hall.getRows();
    }
}
