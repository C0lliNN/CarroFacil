package com.raphaelcollin.customerservice.core;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Customer {
    private String id;
    private String name;
    private String userId;
    private int bookingsCount;

    public void incrementBookingsCount() {
        bookingsCount++;
    }
}
