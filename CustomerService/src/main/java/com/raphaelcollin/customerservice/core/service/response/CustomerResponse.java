package com.raphaelcollin.customerservice.core.service.response;


import com.raphaelcollin.customerservice.core.Customer;
import com.raphaelcollin.customerservice.core.User;

public record CustomerResponse(String id, String name, String email, int bookingsCount, String token) {
    public static CustomerResponse fromCustomer(Customer customer, User user) {
        return new CustomerResponse(customer.getId(), customer.getName(), user.getEmail(), customer.getBookingsCount(), user.getToken());
    }
}
