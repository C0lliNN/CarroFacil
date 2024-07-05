package com.raphaelcollin.bookingservice.infrastructure.web.filter;

import com.raphaelcollin.bookingservice.infrastructure.web.User;

public interface AuthorizationClient {
    User validateToken(String token);
}
