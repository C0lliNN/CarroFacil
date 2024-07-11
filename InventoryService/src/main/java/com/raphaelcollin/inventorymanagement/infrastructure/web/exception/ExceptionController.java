package com.raphaelcollin.inventorymanagement.infrastructure.web.exception;

import com.raphaelcollin.inventorymanagement.core.exception.EntityNotFoundException;
import com.raphaelcollin.inventorymanagement.core.exception.InvalidTokenException;
import com.raphaelcollin.inventorymanagement.core.exception.VehicleAlreadyBookedException;
import com.raphaelcollin.inventorymanagement.core.exception.VehicleNotAvailableException;
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

    @ExceptionHandler(VehicleAlreadyBookedException.class)
    public ResponseEntity<ExceptionBody> handleVehicleAlreadyBookedException(VehicleAlreadyBookedException e) {
        log.error("An attempt to book a vehicle that is already booked was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionBody.fromMessage(e.getMessage()));
    }

    @ExceptionHandler(VehicleNotAvailableException.class)
    public ResponseEntity<ExceptionBody> handleVehicleNotAvailableException(VehicleNotAvailableException e) {
        log.error("An attempt to book a vehicle that is not available was made: {}", e.getMessage(), e);

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

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionBody> handleGeneralError(Throwable throwable) {
        log.error("An unexpected error was thrown when performing the request: {}", throwable.getMessage(), throwable);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionBody.fromMessage(GENERIC_SERVER_ERROR_BODY_MESSAGE));
    }
}
