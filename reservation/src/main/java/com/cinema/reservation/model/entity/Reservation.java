package com.cinema.reservation.model.entity;

import com.cinema.reservation.repository.ReservationRepository;
import jakarta.persistence.*;

@Entity
@Table(name = "RESERVATIONS")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Long totalPrice = 0L;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;


    public Reservation() {
        this.totalPrice = 0L;
        this.status = ReservationStatus.RESERVED;
    }

    public Reservation(String username) {
        this.username = username;
        this.totalPrice = 0L;
        this.status = ReservationStatus.RESERVED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
