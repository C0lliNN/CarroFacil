package com.raphaelcollin.usermanagement.infrastructure.queue;

import com.raphaelcollin.usermanagement.core.User;
import com.raphaelcollin.usermanagement.core.UserRepository;
import com.raphaelcollin.usermanagement.infrastructure.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class SQSListenerTest extends IntegrationTest {
    @Autowired
    private UserRepository repository;

    @Nested
    class ListenMethod {

        @Test
        void whenMessageIsValid_shouldIncrementUserBookingsCount() throws ExecutionException, InterruptedException {
            User user = User.builder()
                    .name("name")
                    .type(User.Type.CUSTOMER)
                    .email("email")
                    .password("password")
                    .bookingsCount(5)
                    .build();
            repository.save(user);

            MessageBody body = new MessageBody("{\"userId\":\"%s\",\"bookingId\":2}".formatted(user.getId()));


            sqsClient.sendMessage(r -> {
                try {
                    r.queueUrl("http://localhost:4566/000000000000/bookings")
                            .messageBody(new ObjectMapper().writeValueAsString(body));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).get();

            await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
                User result = repository.findById(user.getId()).get();
                assertEquals(6, result.getBookingsCount());
            });
        }
    }

}