package com.raphaelcollin.usermanagement.infrastructure.token;

import com.raphaelcollin.usermanagement.core.User;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenManagerTest {

    @Test
    void testJwtTokenManager() {
        String secret = "qKzO4DG1pp5m-_6Desp_t5tjuLbmp059porvUP3ipfwcKIfAw2D-_aqKQdqoS-3iY_3jYp25wk8vWi_S8ViUVQ";
        JwtTokenManager jwtTokenManager = new JwtTokenManager(secret, 86400000L);

        String id = "id";
        String name = "name";
        String email = "email";
        String type = "CUSTOMER";

        var user = new User(id, name, User.Type.valueOf(type), email, null);
        String token = jwtTokenManager.generateTokenForUser(user);
        var extractedUser = jwtTokenManager.extractUserFromToken(token);

        assertFalse(token.isEmpty());
        assertEquals(id, extractedUser.getId());
        assertEquals(name, extractedUser.getName());
        assertEquals(email, extractedUser.getEmail());
        assertEquals(type, extractedUser.getType().name());
    }
}