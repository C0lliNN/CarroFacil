package com.raphaelcollin.employeeservice.infrastructure.web;

import com.raphaelcollin.employeeservice.core.service.EmployeeService;
import com.raphaelcollin.employeeservice.core.service.request.LoginRequest;
import com.raphaelcollin.employeeservice.core.service.request.RegisterRequest;
import com.raphaelcollin.employeeservice.core.service.response.EmployeeResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/register")
    EmployeeResponse register(@RequestBody @Valid RegisterRequest request) {
        return employeeService.register(request);
    }

    @PostMapping("/login")
    EmployeeResponse login(@RequestBody @Valid LoginRequest request) {
        return employeeService.login(request);
    }
}
