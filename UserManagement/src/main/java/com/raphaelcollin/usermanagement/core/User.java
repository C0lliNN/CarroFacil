package com.raphaelcollin.usermanagement.core;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class User {
    private String id;
    private String name;
    private Type type;
    private String email;
    private String password;

    public User() {
    }

    public User(String id, String name, Type type, String email, String password) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public enum Type {
        EMPLOYEE,
        CUSTOMER
    }
}
