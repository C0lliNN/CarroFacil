package com.raphaelcollin.usermanagement.core.exception;
import java.io.Serial;

public class IncorrectPasswordException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public IncorrectPasswordException(final String message) {
        super(message);
    }
}
