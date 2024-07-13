package com.raphaelcollin.employeeservice.core;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Employee {
    private String id;
    private String name;
    private String userId;
}
