package com.cinema.reservation.repository;

import com.cinema.reservation.model.entity.Place;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlaceRepository extends CrudRepository<Place, Long> {
    List<Place> findPlacesByFilmEventId(Long eventId);
    Place findPlacesByFilmEventIdAndColumnAndRow(Long eventId, Long column, Long row);

    @Modifying
    @Query("""
    update Place p
       set p.status = PlaceStatus.BOOKED, p.reservationId = :reservationId
     where p.id = :placeId and p.status = PlaceStatus.EMPTY
    """)
    int bookPlace(@Param("placeId") Long placeId, @Param("reservationId") Long reservationId);

    @Modifying
    @Query("""
    update Place p
       set p.status = PlaceStatus.EMPTY, p.reservationId = null
     where p.status = PlaceStatus.BOOKED and p.reservationId = :reservationId
    """)
    void unbookPlacesByReservation(@Param("reservationId") Long reservationId);
}
