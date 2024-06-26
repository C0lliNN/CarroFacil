package com.raphaelcollin.inventorymanagement.infrastructure.client;

import com.raphaelcollin.inventorymanagement.core.exception.InvalidTokenException;
import com.raphaelcollin.inventorymanagement.infrastructure.web.User;
import com.raphaelcollin.inventorymanagement.infrastructure.web.filter.AuthorizationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class RestAuthorizationClient implements AuthorizationClient {
    private final RestTemplate restTemplate;

    @Value("${auth.authorizationserver.url}")
    private String endpoint;

    @Override
    public User validateToken(String token) {
        try {
            Map<String, String> body = Map.of("token", token);
            ResponseEntity<User> response = restTemplate.postForEntity(endpoint, body, User.class);
            if (response.getStatusCode().isError()) {
                throw new InvalidTokenException("Invalid token");
            }

            return response.getBody();
        } catch (Exception e) {
            log.error("An error occurred while validating token: {}", e.getMessage(), e);
            throw new InvalidTokenException("Invalid token");
        }

    }
}
