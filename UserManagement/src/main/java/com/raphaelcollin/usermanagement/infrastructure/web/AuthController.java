package com.raphaelcollin.usermanagement.infrastructure.web;

import com.raphaelcollin.usermanagement.core.Authenticator;
import com.raphaelcollin.usermanagement.core.request.LoginRequest;
import com.raphaelcollin.usermanagement.core.request.RegisterRequest;
import com.raphaelcollin.usermanagement.core.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final Authenticator useCase;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid RegisterRequest request) {
        return useCase.register(request);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody @Valid LoginRequest request) {
        return useCase.login(request);
    }
}
