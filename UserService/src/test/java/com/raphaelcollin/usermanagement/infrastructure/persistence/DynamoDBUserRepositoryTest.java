package com.raphaelcollin.usermanagement.infrastructure.persistence;

import com.raphaelcollin.usermanagement.core.User;
import com.raphaelcollin.usermanagement.infrastructure.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DynamoDBUserRepositoryTest extends IntegrationTest {
    @Autowired
    private DynamoDBUserRepository dynamoDBUserRepository;

    @Nested
    class FindByIdMethod {
        @Test
        void whenUserExists_shouldReturnUser() {
            User user = User.builder()
                    .name("name")
                    .email("email")
                    .password("password")
                    .type("CUSTOMER")
                    .build();

            dynamoDBUserRepository.save(user);

            Optional<User> result = dynamoDBUserRepository.findById(user.getId());

            assertTrue(result.isPresent());
            assertEquals(user.getName(), result.get().getName());
            assertEquals(user.getEmail(), result.get().getEmail());
            assertEquals(user.getPassword(), result.get().getPassword());
        }

        @Test
        void whenUserDoesNotExist_shouldReturnEmpty() {
            Optional<User> result = dynamoDBUserRepository.findById("userId");

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByEmailMethod {
        @Test
        void whenUserExists_shouldReturnUser() {
            User user = User.builder()
                    .name("name")
                    .email("email")
                    .password("password")
                    .type("CUSTOMER")
                    .build();

            dynamoDBUserRepository.save(user);

            Optional<User> result = dynamoDBUserRepository.findByEmail(user.getEmail());

            assertTrue(result.isPresent());
            assertEquals(user.getName(), result.get().getName());
            assertEquals(user.getEmail(), result.get().getEmail());
            assertEquals(user.getPassword(), result.get().getPassword());
        }

        @Test
        void whenUserDoesNotExist_shouldReturnEmpty() {
            Optional<User> result = dynamoDBUserRepository.findByEmail("email");

            assertTrue(result.isEmpty());
        }
    }
}