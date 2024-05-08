package com.raphaelcollin.usermanagement.core;

public interface TokenGenerator {
    String generateTokenForUser(User user);
}
