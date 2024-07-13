package com.raphaelcollin.customerservice.infrastructure.web;

import com.raphaelcollin.customerservice.core.service.request.LoginRequest;
import com.raphaelcollin.customerservice.core.service.request.RegisterRequest;
import com.raphaelcollin.customerservice.core.service.CustomerService;
import com.raphaelcollin.customerservice.core.service.response.CustomerResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    CustomerResponse register(@RequestBody @Valid RegisterRequest request) {
        return customerService.register(request);
    }

    @PostMapping("/login")
    CustomerResponse login(@RequestBody @Valid LoginRequest request) {
        return customerService.login(request);
    }
}
