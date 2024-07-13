package com.raphaelcollin.employeeservice.core.service;


import com.raphaelcollin.employeeservice.core.User;

public interface UserServiceClient {
    User login(String email, String password);
    User register(String name, String email, String password);
}
