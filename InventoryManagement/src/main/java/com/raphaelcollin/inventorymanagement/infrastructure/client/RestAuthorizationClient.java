package com.raphaelcollin.inventorymanagement.infrastructure.client;

import com.raphaelcollin.inventorymanagement.core.exception.InvalidTokenException;
import com.raphaelcollin.inventorymanagement.infrastructure.web.filter.AuthorizationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class RestAuthorizationClient implements AuthorizationClient {
    private final RestTemplate restTemplate;

    @Value("${auth.authorizationserver.url}")
    private String endpoint;

    @Override
    public void validateToken(String token) {
        ResponseEntity<Void> response = restTemplate.postForEntity(endpoint, token, Void.class);
        if (response.getStatusCode().isError()) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}
