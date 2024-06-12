package com.raphaelcollin.inventorymanagement.core;

import java.util.List;
import java.util.Optional;

public interface StoreRepository {
    Store save(Store store);
    List<Store> findAll();
    Optional<Store> findById(int id);
}
