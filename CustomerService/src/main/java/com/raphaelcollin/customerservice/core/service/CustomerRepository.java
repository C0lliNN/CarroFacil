package com.raphaelcollin.customerservice.core.service;

import com.raphaelcollin.customerservice.core.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findByUserId(String userId);
}
