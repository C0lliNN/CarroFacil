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
    private String type;
    private String email;
    private String password;
}
