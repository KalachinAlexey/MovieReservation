package com.cinema.reservation.repository;

import com.cinema.reservation.model.entity.Reservation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    @Modifying
    @Query("""
    update Reservation r
       set r.totalPrice = r.totalPrice + :addPrice
     where r.id = :reservationId
    """)
    void addReservationPrice(@Param("addPrice") Long addPrice, @Param("reservationId") Long reservationId);

    @Modifying
    @Query("""
    update Reservation r
       set r.status = ReservationStatus.PAID
     where r.id = :reservationId and r.status = ReservationStatus.RESERVED
    """)
    void payReservationById(@Param("reservationId") Long reservationId);

    @Modifying
    @Query("""
    update Reservation r
       set r.status = ReservationStatus.CANCELLED
     where r.id = :reservationId and r.status = ReservationStatus.RESERVED
    """)
    void cancelReservationById(@Param("reservationId") Long reservationId);

    Reservation findReservationById(Long id);
}
