package com.raphaelcollin.usermanagement.core;

public interface TokenManager {
    String generateTokenForUser(User user);
    User extractUserFromToken(String token);
}
