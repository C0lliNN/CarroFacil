package com.raphaelcollin.employeeservice.core.service;

import com.raphaelcollin.employeeservice.core.Employee;
import com.raphaelcollin.employeeservice.core.User;
import com.raphaelcollin.employeeservice.core.service.request.RegisterRequest;
import com.raphaelcollin.employeeservice.core.service.response.EmployeeResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final UserServiceClient userServiceClient;
    private final EmployeeRepository employeeRepository;

    public EmployeeResponse register(RegisterRequest request) {
        User user = userServiceClient.register(request.name(), request.email(), request.password());

        Employee employee = Employee.builder()
                .name(request.name())
                .userId(user.getId())
                .build();

        return EmployeeResponse.fromEmployee(employeeRepository.save(employee), user);
    }
}
