package com.raphaelcollin.customerservice.infrastructure.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphaelcollin.customerservice.core.service.CustomerService;
import com.raphaelcollin.usermanagement.core.BookingsCounter;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
@Slf4j
@AllArgsConstructor
public class SQSListener {
    private final CustomerService service;
    private final ObjectMapper objectMapper;

    @SqsListener(queueNames = "${aws.queues.bookings}")
    public void listen(Message message) {
        try {
            MessageBody body = objectMapper.readValue(message.body(), MessageBody.class);
            BookingMessage bookingMessage = objectMapper.readValue(body.getMessage(), BookingMessage.class);

            log.info("Received message: {}", bookingMessage);

            service.incrementBookingsCount(bookingMessage.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("Error while processing message", e);
        }
    }
}
