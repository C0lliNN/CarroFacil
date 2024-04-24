package com.raphaelcollin.usermanagement.infrastructure;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SQSListener {

    @SqsListener(queueNames = "${aws.queues.bookings}")
    public void listen(String message) {
        System.out.println(message);
    }
}
