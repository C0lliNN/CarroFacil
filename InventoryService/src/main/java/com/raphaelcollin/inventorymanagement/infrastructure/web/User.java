package com.raphaelcollin.inventorymanagement.infrastructure.web;

public record User(
        String id,
        String name,
        String type,
        String email,
        String token
) {
}
