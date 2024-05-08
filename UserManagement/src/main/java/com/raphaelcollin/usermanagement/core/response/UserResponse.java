package com.raphaelcollin.usermanagement.core.response;


import com.raphaelcollin.usermanagement.core.User;

public record UserResponse(String id, String name, String email, String token) {

    public UserResponse withToken(String token) {
        return new UserResponse(id, name, email, token);
    }

    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), null);
    }
}
