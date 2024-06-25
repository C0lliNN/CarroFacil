package com.raphaelcollin.inventorymanagement.infrastructure.client;

import com.raphaelcollin.inventorymanagement.core.exception.InvalidTokenException;
import com.raphaelcollin.inventorymanagement.infrastructure.web.filter.AuthorizationClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Component
public class RestAuthorizationClient implements AuthorizationClient {
    private RestTemplate restTemplate;

    @Override
    public void validateToken(String token) {
        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:8080/auth/validate", token, Void.class);
        if (response.getStatusCode().isError()) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}
