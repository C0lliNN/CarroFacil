package com.raphaelcollin.usermanagement.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingsCounterTest {

    @InjectMocks
    private BookingsCounter bookingsCounter;

    @Mock
    private UserRepository userRepository;

    @Nested
    @DisplayName("method: incrementUserBookingsCount(String)")
    class IncrementUserBookingsCountMethod {

        @Test
        @DisplayName("when user is found, should increment bookings count and save user")
        void whenUserIsFound_shouldIncrementBookingsCountAndSaveUser() {
            String userId = "userId";
            User user = User.builder()
                    .id(userId)
                    .build();

            when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

            bookingsCounter.incrementUserBookingsCount(userId);

            assertEquals(1, user.getBookingsCount());
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("when user is not found, should throw IllegalArgumentException")
        void whenUserIsNotFound_shouldThrowIllegalArgumentException() {
            String userId = "userId";

            when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> bookingsCounter.incrementUserBookingsCount(userId));
        }
    }
}