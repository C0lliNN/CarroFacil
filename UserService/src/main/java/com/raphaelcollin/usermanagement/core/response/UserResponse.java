package com.raphaelcollin.usermanagement.core.response;


import com.raphaelcollin.usermanagement.core.User;

public record UserResponse(String id, String name, User.Type type, String email, String token) {

    public UserResponse withToken(String token) {
        return new UserResponse(id, name, type, email, token);
    }

    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getType(), user.getEmail(), null);
    }
}
