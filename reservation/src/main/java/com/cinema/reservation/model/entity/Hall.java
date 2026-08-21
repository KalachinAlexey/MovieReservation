package com.cinema.reservation.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HALLS")
public class Hall {
    @Id
    private Long id;

    private Long rows, columns;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        this.rows = rows;
    }

    public Long getColumns() {
        return columns;
    }

    public void setColumns(Long columns) {
        this.columns = columns;
    }
}
