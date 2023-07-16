package ru.practicum.server.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> catchBadRequestException(final BadRequestException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> catchNotFoundException(final NotFoundException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }
}
