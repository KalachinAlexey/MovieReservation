package com.cinema.reservation.controller;

import com.cinema.reservation.errors.CancelReservationException;
import com.cinema.reservation.errors.PayReservationException;
import com.cinema.reservation.errors.PlaceAlreadyBookedException;
import com.cinema.reservation.errors.PlaceValidationException;
import com.cinema.reservation.errors.notfound.EntityElementNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EntityElementNotFound.class)
    ResponseEntity<ProblemDetail> handleNotFound (EntityElementNotFound exception){
        return createDefaultResponse(exception, HttpStatus.NOT_FOUND,
                "Element not found", "ELEMENT_NOT_FOUND");
    }

    @ExceptionHandler(CancelReservationException.class)
    ResponseEntity<ProblemDetail> handleCancel(CancelReservationException exception) {
        return createDefaultResponse(exception, HttpStatus.BAD_REQUEST,
                "Cancellation troubles", "CANCELLATION_TROUBLES");
    }

    @ExceptionHandler(PayReservationException.class)
    ResponseEntity<ProblemDetail> handlePay(PayReservationException exception) {
        return createDefaultResponse(exception, HttpStatus.BAD_REQUEST,
                "Payment troubles", "PAYMENT_TROUBLES");
    }

    @ExceptionHandler(PlaceAlreadyBookedException.class)
    ResponseEntity<ProblemDetail> handleBookingConflict(PlaceAlreadyBookedException exception) {
        return createDefaultResponse(exception, HttpStatus.CONFLICT,
                "Place already booked", "ALREADY_BOOKED");
    }

    @ExceptionHandler(PlaceValidationException.class)
    ResponseEntity<ProblemDetail> handlePay(PlaceValidationException exception) {
        return createDefaultResponse(exception, HttpStatus.BAD_REQUEST,
                "Place information not valid", "INVALID_PLACE");
    }


    private ResponseEntity<ProblemDetail> createDefaultResponse(
            RuntimeException exception,
            HttpStatus httpStatus,
            String title,
            String code
    ) {
        ProblemDetail detail = ProblemDetail.forStatus(httpStatus);
        detail.setTitle(title);
        detail.setDetail(exception.getMessage());
        detail.setProperty("code", code);

        return ResponseEntity.status(httpStatus).body(detail);
    }
}
