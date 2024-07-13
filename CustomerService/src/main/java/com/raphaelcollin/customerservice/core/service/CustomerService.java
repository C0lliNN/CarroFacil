package com.raphaelcollin.customerservice.core.service;

import com.raphaelcollin.customerservice.core.Customer;
import com.raphaelcollin.customerservice.core.User;
import com.raphaelcollin.customerservice.core.exception.EntityNotFoundException;
import com.raphaelcollin.customerservice.core.service.request.LoginRequest;
import com.raphaelcollin.customerservice.core.service.request.RegisterRequest;
import com.raphaelcollin.customerservice.core.service.response.CustomerResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {
    private final UserServiceClient userServiceClient;
    private final CustomerRepository customerRepository;

    public CustomerResponse register(RegisterRequest request) {
        User user = userServiceClient.register(request.name(), request.email(), request.password() );

        Customer customer = Customer.builder()
                .name(request.name())
                .userId(user.getId())
                .build();

        return CustomerResponse.fromCustomer(customerRepository.save(customer), user);
    }

    public CustomerResponse login(LoginRequest request) {
        User user = userServiceClient.login(request.email(), request.password());

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return CustomerResponse.fromCustomer(customer, user);
    }

    public void incrementBookingsCount(String userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        customer.incrementBookingsCount();
        customerRepository.save(customer);
    }
}
