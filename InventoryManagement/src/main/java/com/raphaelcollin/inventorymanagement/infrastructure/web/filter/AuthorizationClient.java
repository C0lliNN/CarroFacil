package com.raphaelcollin.inventorymanagement.infrastructure.web.filter;

import com.raphaelcollin.inventorymanagement.infrastructure.web.User;

public interface AuthorizationClient {
    User validateToken(String token);
}
