package com.raphaelcollin.inventorymanagement.infrastructure.web.filter;

public interface AuthorizationClient {
    void validateToken(String token);
}
