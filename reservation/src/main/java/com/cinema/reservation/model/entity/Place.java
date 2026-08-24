package com.cinema.reservation.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PLACES")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long filmEventId;
    @Column(name = "row_number")
    private Long row;
    @Column(name = "column_number")
    private Long column;
    @Enumerated(EnumType.STRING)
    private PlaceStatus status;
    private Long reservationId;
    private Long price;

    public Place() {}

    public Place(long filmEventId, long row, long column) {
        this.filmEventId = filmEventId;
        this.row = row;
        this.column = column;
        this.status = PlaceStatus.EMPTY;
        this.price = 100L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFilmEventId() {
        return filmEventId;
    }

    public void setFilmEventId(Long filmEventId) {
        this.filmEventId = filmEventId;
    }

    public Long getRow() {
        return row;
    }

    public void setRow(Long row) {
        this.row = row;
    }

    public Long getColumn() {
        return column;
    }

    public void setColumn(Long column) {
        this.column = column;
    }

    public PlaceStatus getStatus() {
        return status;
    }

    public void setStatus(PlaceStatus status) {
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
