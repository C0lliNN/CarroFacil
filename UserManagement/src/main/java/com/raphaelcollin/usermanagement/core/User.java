package com.raphaelcollin.usermanagement.core;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    private String id;
    private String name;
    private Type type;
    private String email;
    private String password;
    private int bookingsCount;

    public void incrementBookingsCount() {
        bookingsCount++;
    }

    public enum Type {
        EMPLOYEE,
        CUSTOMER
    }
}
