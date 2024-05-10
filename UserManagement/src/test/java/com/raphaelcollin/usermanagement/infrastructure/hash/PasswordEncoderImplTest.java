package com.raphaelcollin.usermanagement.infrastructure.hash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderImplTest {

    @Test
    void testPasswordEncoder() {
        PasswordEncoderImpl passwordEncoder = new PasswordEncoderImpl();

        String password = "password";
        String hash = passwordEncoder.hashPassword(password);

        assertFalse(hash.isEmpty());
        assertTrue(passwordEncoder.comparePasswordAndHash(password, hash));
    }
}