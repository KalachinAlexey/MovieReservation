package com.cinema.reservation.repository;

import com.cinema.reservation.model.entity.Place;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends CrudRepository<Place, Long> {
    @Query("""
        select p from Place p
            where p.id = :id and 
                  p.filmEventId = :filmEventId and
                  p.row = :row and
                  p.column = :column  
    """)
    Optional<Place> findValidPlace(@Param("id") Long id, @Param("filmEventId") Long filmEventId,
                                   @Param("row") Long row, @Param("column") Long column);
    List<Place> findPlacesByFilmEventId(Long eventId);

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
