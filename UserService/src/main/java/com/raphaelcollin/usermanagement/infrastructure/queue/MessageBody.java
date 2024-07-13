package com.raphaelcollin.usermanagement.infrastructure.queue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class MessageBody {
    @JsonProperty("Message")
    String message;

    @JsonCreator
    public MessageBody(@JsonProperty("Message") String message) {
        this.message = message;
    }

}
