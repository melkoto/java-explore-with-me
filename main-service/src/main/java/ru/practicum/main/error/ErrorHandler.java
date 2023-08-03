package ru.practicum.main.error;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> catchBadRequestException(final BadRequestException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> catchConflictException(final ConflictException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> catchNotFoundException(final NotFoundException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchPSQLException(final PSQLException e) {
        log.error(e.getMessage(), e);
        HttpStatus httpStatus = CONFLICT;
        if (e.getMessage().contains("null value in column \"email\"")) {
            httpStatus = BAD_REQUEST;
        }
        return new ResponseEntity<>(new ErrorResponse(httpStatus.value(), e.getMessage()), httpStatus);
    }
}
