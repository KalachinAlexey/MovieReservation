package com.cinema.reservation.repository;

import com.cinema.reservation.model.entity.Reservation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
     where r.id = :reservationId and r.username = :username and r.status = ReservationStatus.RESERVED
    """)
    int payReservationById(@Param("reservationId") Long reservationId, @Param("username") String username);

    @Modifying
    @Query("""
    update Reservation r
       set r.status = ReservationStatus.CANCELLED
     where r.id = :reservationId and r.username = :username and r.status = ReservationStatus.RESERVED
    """)
    int cancelReservationById(@Param("reservationId") Long reservationId, @Param("username") String username);

    Optional<Reservation> findReservationById(Long id);
    Optional<Reservation> findReservationByIdAndUsername(Long id, String username);

    List<Reservation> findReservationsByUsername(String username);
}
