package com.raphaelcollin.employeeservice.core.service;


import com.raphaelcollin.employeeservice.core.User;

public interface UserServiceClient {
    User register(String name, String email, String password);
}
