package com.cinema.reservation.service;

import com.cinema.reservation.errors.*;
import com.cinema.reservation.errors.notfound.EventNotFound;
import com.cinema.reservation.errors.notfound.HallNotFound;
import com.cinema.reservation.errors.notfound.ReservationNotFound;
import com.cinema.reservation.kafka.NotificationProducer;
import com.cinema.reservation.model.dto.PlaceDto;
import com.cinema.reservation.model.entity.*;
import com.cinema.reservation.repository.EventRepository;
import com.cinema.reservation.repository.HallRepository;
import com.cinema.reservation.repository.PlaceRepository;
import com.cinema.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ReservationServiceImpl implements ReservationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final PlaceRepository placeRepository;
    private final HallRepository hallRepository;
    private final EventRepository eventRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationProducer notificationProducer;

    public ReservationServiceImpl(PlaceRepository placeRepository,
                                  HallRepository hallRepository,
                                  EventRepository eventRepository, ReservationRepository reservationRepository, NotificationProducer notificationProducer) {
        this.placeRepository = placeRepository;
        this.hallRepository = hallRepository;
        this.eventRepository = eventRepository;
        this.reservationRepository = reservationRepository;
        this.notificationProducer = notificationProducer;
    }

    @Override
    public List<Place> findPlacesByEventId(long eventId) {
        return placeRepository.findPlacesByFilmEventId(eventId);
    }

    @Override
    @Transactional
    public Reservation bookPlaces(List<PlaceDto> places, String username) {
        Reservation reservation = reservationRepository.save(new Reservation(username));

        places.forEach(placeDto -> {
                Place place = placeRepository.findValidPlace(
                        placeDto.id(), placeDto.filmEventId(),
                        placeDto.row(), placeDto.column())
                    .orElseThrow(() -> new PlaceValidationException(placeDto.id()));

                Long hallId = eventRepository.findFilmEventById(place.getFilmEventId())
                        .orElseThrow(() -> new EventNotFound(place.getFilmEventId()))
                        .getHallId();
                Hall hall = hallRepository.findHallById(hallId)
                        .orElseThrow(() -> new HallNotFound(hallId));

                if (!validatePlace(place, hall)) {
                    throw new PlaceValidationException(place.getId());
                }
                if (placeRepository.bookPlace(place.getId(), reservation.getId()) == 0) {
                    throw new PlaceAlreadyBookedException(place.getId());
                }

                reservation.setTotalPrice(reservation.getTotalPrice() + place.getPrice());
                reservationRepository.addReservationPrice(place.getPrice(), reservation.getId());
        });
        LOGGER.info("{}", reservation.getTotalPrice());
        notificationProducer.send(reservation);
        return reservation;
    }

    @Override
    @Transactional
    public void addPlacesForEvent(FilmEvent event) {
        Hall hall = hallRepository.findHallById(event.getHallId())
                .orElseThrow(() -> new HallNotFound(event.getHallId()));
        for (int i = 1; i <= hall.getRows(); i++) {
            for (int j = 1; j <= hall.getColumns(); j++) {
                placeRepository.save(new Place(event.getId(), i, j));
            }
        }
    }

    @Override
    @Transactional
    public Reservation payReservation(Long reservationId, String username) {
        int updated = reservationRepository.payReservationById(reservationId, username);
        if (updated == 0) {
            throw new PayReservationException(reservationId);
        }
        Reservation reservation = reservationRepository.findReservationById(reservationId)
                .orElseThrow(() -> new ReservationNotFound(reservationId));
        notificationProducer.send(reservation);
        return reservation;
    }

    @Override
    @Transactional
    public Reservation cancelReservation(Long reservationId, String username) {
        int updated = reservationRepository.cancelReservationById(reservationId, username);
        if (updated == 0) {
            throw new CancelReservationException(reservationId);
        }
        placeRepository.unbookPlacesByReservation(reservationId);
        Reservation reservation = reservationRepository.findReservationById(reservationId)
                .orElseThrow(() -> new ReservationNotFound(reservationId));
        notificationProducer.send(reservation);
        return reservation;
    }

    private boolean validatePlace(Place place, Hall hall) {
        return 1 <= place.getColumn() && place.getColumn() <= hall.getColumns() &&
                1 <= place.getRow() && place.getRow() <= hall.getRows();
    }
}
