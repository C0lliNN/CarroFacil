package com.raphaelcollin.inventorymanagement.core.service;

public record StoreResponse(
        int id,
        String name,
        String address,
        String phoneNumber,
        long latitude,
        long longitude
) {
    public static StoreResponse fromStore(com.raphaelcollin.inventorymanagement.core.Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getLatitude(),
                store.getLongitude()
        );
    }
}
