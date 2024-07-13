package com.raphaelcollin.customerservice.core;

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
