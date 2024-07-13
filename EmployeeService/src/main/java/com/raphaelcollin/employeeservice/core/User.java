package com.raphaelcollin.employeeservice.core;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    String id;
    String email;
    String token;
}
