package com.raphaelcollin.bookingservice.infrastructure.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphaelcollin.bookingservice.core.BookingEvent;
import com.raphaelcollin.bookingservice.core.service.BookingEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class SNSEventPublisher implements BookingEventPublisher {
    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Value("${sns.topic.bookings}")
    private String topicArn;

    @SneakyThrows
    @Override
    public void publishBookingEvent(BookingEvent bookingEvent) {
        String message = objectMapper.writeValueAsString(bookingEvent);

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .build();

        PublishResponse response = snsClient.publish(request);

        log.info("Message published to SNS with message id: {}", response.messageId());
    }
}
