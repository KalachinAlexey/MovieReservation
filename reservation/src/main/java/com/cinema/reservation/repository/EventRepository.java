package com.cinema.reservation.repository;

import com.cinema.reservation.model.entity.FilmEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<FilmEvent, Long> {
    List<FilmEvent> findAll();
    FilmEvent findFilmEventById(Long id);
}
