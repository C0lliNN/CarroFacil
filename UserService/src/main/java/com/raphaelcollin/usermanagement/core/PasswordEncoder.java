package com.raphaelcollin.usermanagement.core;

public interface PasswordEncoder {
    String hashPassword(String password);
    boolean comparePasswordAndHash(String password, String hash);
}
