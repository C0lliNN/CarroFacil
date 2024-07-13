package com.raphaelcollin.customerservice.core.service;

import com.raphaelcollin.customerservice.core.User;

public interface UserServiceClient {
    User register(String name, String email, String password);
}
