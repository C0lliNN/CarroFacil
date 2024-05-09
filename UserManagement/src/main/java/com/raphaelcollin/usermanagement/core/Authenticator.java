package com.raphaelcollin.usermanagement.core;

import com.raphaelcollin.usermanagement.core.exception.DuplicateEmailException;
import com.raphaelcollin.usermanagement.core.exception.EmailNotFoundException;
import com.raphaelcollin.usermanagement.core.exception.IncorrectPasswordException;
import com.raphaelcollin.usermanagement.core.request.LoginRequest;
import com.raphaelcollin.usermanagement.core.request.RegisterRequest;
import com.raphaelcollin.usermanagement.core.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Authenticator {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public UserResponse login(LoginRequest request) {
        User user = repository.findByEmail(request.email()).orElseThrow(() -> new EmailNotFoundException("The email '%s' could not be found.", request.email()));

        if (!passwordEncoder.comparePasswordAndHash(request.password(), user.getPassword())) {
            throw new IncorrectPasswordException("The provided password is incorrect.");
        }

        return createUserResponseWithToken(user);
    }

    public UserResponse register(RegisterRequest request) {
        User user = request.toUser();

        repository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new DuplicateEmailException("The email '%s' is already in use.".formatted(existingUser.getEmail()));
        });

        user.setPassword(passwordEncoder.hashPassword(user.getPassword()));
        user = repository.save(user);

        return createUserResponseWithToken(user);
    }

    private UserResponse createUserResponseWithToken(User user) {
        String token = tokenGenerator.generateTokenForUser(user);

        return UserResponse.fromUser(user).withToken(token);
    }
}
