package com.raphaelcollin.usermanagement.core;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookingsCounter {
    private final UserRepository userRepository;

    public void incrementUserBookingsCount(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.incrementBookingsCount();
        userRepository.save(user);
    }
}
