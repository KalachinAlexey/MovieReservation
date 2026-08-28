package com.cinema.reservation.repository;

import com.cinema.reservation.model.entity.FilmEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<FilmEvent, Long> {
    List<FilmEvent> findAll();
    Optional<FilmEvent> findFilmEventById(Long id);
}
