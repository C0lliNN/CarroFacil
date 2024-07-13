package com.raphaelcollin.customerservice.infrastructure.client;

import com.raphaelcollin.customerservice.core.User;
import com.raphaelcollin.customerservice.core.service.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RestUserServiceClient implements UserServiceClient {
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String endpoint;

    @Override
    public User register(String name, String email, String password) {
        Map<String, String> body = Map.of("name", name, "email", email, "password", password, "type", "CUSTOMER");
        return restTemplate.postForObject(endpoint + "/register", body, User.class);
    }
}
