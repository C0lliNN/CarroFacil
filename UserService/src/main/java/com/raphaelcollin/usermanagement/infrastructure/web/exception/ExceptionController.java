package com.raphaelcollin.usermanagement.infrastructure.web.exception;

import com.raphaelcollin.usermanagement.core.exception.DuplicateEmailException;
import com.raphaelcollin.usermanagement.core.exception.EmailNotFoundException;
import com.raphaelcollin.usermanagement.core.exception.IncorrectPasswordException;
import com.raphaelcollin.usermanagement.core.exception.InvalidTokenException;
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

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ExceptionBody> handleDuplicateEmailException(DuplicateEmailException e) {
        log.error("An attempt to create a user with existing email was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleEmailNotFoundException(EmailNotFoundException e) {
        log.error("An attempt to login with a not found email was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ExceptionBody> handleIncorrectPasswordException(IncorrectPasswordException e) {
        log.error("An attempt to login with a wrong password was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionBody> handleInvalidTokenException(InvalidTokenException e) {
        log.error("An attempt to perform a request with an invalid token was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionBody> handleGeneralError(Throwable throwable) {
        log.error("An unexpected error was thrown when performing the request: {}", throwable.getMessage(), throwable);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionBody.fromMessage(GENERIC_SERVER_ERROR_BODY_MESSAGE));
    }
}
