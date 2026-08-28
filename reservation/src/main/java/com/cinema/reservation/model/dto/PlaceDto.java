package com.cinema.reservation.model.dto;

public record PlaceDto(
        Long id,
        Long filmEventId,
        Long row,
        Long column
) {
}
