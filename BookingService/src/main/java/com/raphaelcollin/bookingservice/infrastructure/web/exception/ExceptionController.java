package com.raphaelcollin.bookingservice.infrastructure.web.exception;

import com.raphaelcollin.bookingservice.core.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    private static final String GENERIC_SERVER_ERROR_BODY_MESSAGE = "There's been an unexpected error. Please, check logs or contact support.";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("An attempt to modify or create a resource with invalid data was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("An entity was not found: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ExceptionBody.fromMessage(e.getMessage()));
    }

    @ExceptionHandler(InventoryBookingException.class)
    public ResponseEntity<ExceptionBody> handleInventoryBookingException(InventoryBookingException e) {
        log.error("An error occurred while interacting with the inventory management service: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionBody.fromMessage(e.getMessage()));
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<ExceptionBody> handleInvalidBookingStateException(InvalidBookingStateException e) {
        log.error("An invalid booking state was detected: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionBody.fromMessage(e.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionBody> handleInvalidTokenException(InvalidTokenException e) {
        log.error("An invalid token was provided: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionBody.fromMessage(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ExceptionBody> handleAuthorizationException(AuthorizationException e) {
        log.error("An authorization error occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ExceptionBody.fromMessage(e.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionBody> handleGeneralError(Throwable throwable) {
        log.error("An unexpected error was thrown when performing the request: {}", throwable.getMessage(), throwable);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionBody.fromMessage(GENERIC_SERVER_ERROR_BODY_MESSAGE));
    }
}
