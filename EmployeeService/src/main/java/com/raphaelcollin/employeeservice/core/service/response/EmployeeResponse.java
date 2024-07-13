package com.raphaelcollin.employeeservice.core.service.response;


import com.raphaelcollin.employeeservice.core.Employee;
import com.raphaelcollin.employeeservice.core.User;

public record EmployeeResponse(String id, String name, String email, String type, String token) {
    public static EmployeeResponse fromEmployee(Employee employee, User user) {
        return new EmployeeResponse(employee.getId(), employee.getName(), user.getEmail(), user.getType(), user.getToken());
    }
}
