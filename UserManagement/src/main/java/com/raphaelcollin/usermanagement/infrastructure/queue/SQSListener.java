package com.raphaelcollin.usermanagement.infrastructure.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphaelcollin.usermanagement.core.BookingsCounter;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class SQSListener {
    private final BookingsCounter bookingsCounter;
    private final ObjectMapper objectMapper;

    @SqsListener(queueNames = "${aws.queues.bookings}")
    public void listen(String message) {
        try {
            BookingMessage bookingMessage = objectMapper.readValue(message, BookingMessage.class);
            log.info("Received message: {}", bookingMessage);

            bookingsCounter.incrementUserBookingsCount(bookingMessage.userId());
        } catch (Exception e) {
            throw new RuntimeException("Error while processing message", e);
        }
    }
}
