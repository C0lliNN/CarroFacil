package com.raphaelcollin.usermanagement.core.exception;
import java.io.Serial;

public class InvalidTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidTokenException(String message) {
        super(message);
    }
}
