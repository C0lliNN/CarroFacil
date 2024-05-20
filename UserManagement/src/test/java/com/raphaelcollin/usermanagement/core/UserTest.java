package com.raphaelcollin.usermanagement.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void incrementBookingsCount() {
        User user = User.builder()
                .id("id")
                .name("name")
                .type(User.Type.CUSTOMER)
                .email("email")
                .password("password")
                .bookingsCount(0)
                .build();

        user.incrementBookingsCount();
        assertEquals(1, user.getBookingsCount());

        user.incrementBookingsCount();
        assertEquals(2, user.getBookingsCount());
    }
}