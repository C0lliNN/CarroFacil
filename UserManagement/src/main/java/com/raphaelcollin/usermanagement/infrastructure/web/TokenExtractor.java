package com.raphaelcollin.usermanagement.infrastructure.web;

import com.raphaelcollin.usermanagement.core.User;

public interface TokenExtractor {
    User extractUserFromToken(String token);
}
