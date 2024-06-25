package com.raphaelcollin.inventorymanagement.infrastructure.persistence;

import com.raphaelcollin.inventorymanagement.IntegrationTest;
import com.raphaelcollin.inventorymanagement.core.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBCStoreRepositoryTest extends IntegrationTest {

    @Autowired
    private JDBCStoreRepository jdbcStoreRepository;

    @Nested
    @DisplayName("save method")
    class SaveMethod {

        @Test
        @DisplayName("when called and item is not persisted, then it should insert it")
        void whenCalled_thenItShouldSaveTheStore() {
            Store store = new Store(0, "Store", "Address", "123456789", 0L, 0L);

            Store savedStore = jdbcStoreRepository.save(store);

            assertNotEquals(0, savedStore.getId());
            assertEquals(store.getName(), savedStore.getName());
            assertEquals(store.getAddress(), savedStore.getAddress());
            assertEquals(store.getPhoneNumber(), savedStore.getPhoneNumber());
            assertEquals(store.getLatitude(), savedStore.getLatitude());
            assertEquals(store.getLongitude(), savedStore.getLongitude());
            assertTrue(jdbcStoreRepository.findById(savedStore.getId()).isPresent());
        }

        @Test
        @DisplayName("when called and item is persisted, then it should update it")
        void whenCalled_thenItShouldUpdateTheStore() {
            Store store = new Store(0, "Store", "Address", "123456789", 0L, 0L);
            Store savedStore = jdbcStoreRepository.save(store);

            savedStore.setName("Updated Store");
            savedStore.setAddress("Updated Address");
            savedStore.setPhoneNumber("987654321");
            savedStore.setLatitude(1L);
            savedStore.setLongitude(1L);

            Store updatedStore = jdbcStoreRepository.save(savedStore);

            assertEquals(savedStore.getId(), updatedStore.getId());
            assertEquals(savedStore.getName(), updatedStore.getName());
            assertEquals(savedStore.getAddress(), updatedStore.getAddress());
            assertEquals(savedStore.getPhoneNumber(), updatedStore.getPhoneNumber());
            assertEquals(savedStore.getLatitude(), updatedStore.getLatitude());
            assertEquals(savedStore.getLongitude(), updatedStore.getLongitude());
        }
    }

    @Nested
    @DisplayName("findAll method")
    class FindAllMethod {

        @Test
        @DisplayName("when called and there are items persisted, then it should return all of them")
        void whenCalled_thenItShouldReturnAllStores() {
            Store store1 = new Store(0, "Store 1", "Address 1", "123456789", 0L, 0L);
            Store store2 = new Store(0, "Store 2", "Address 2", "987654321", 1L, 1L);
            Store store3 = new Store(0, "Store 3", "Address 3", "123456789", 2L, 2L);

            jdbcStoreRepository.save(store1);
            jdbcStoreRepository.save(store2);
            jdbcStoreRepository.save(store3);

            List<Store> stores = jdbcStoreRepository.findAll();
            assertEquals(3, stores.size());
        }

        @Test
        @DisplayName("when called and there are no items persisted, then it should return an empty list")
        void whenCalled_thenItShouldReturnAnEmptyList() {
            assertTrue(jdbcStoreRepository.findAll().isEmpty());
        }
    }

    @Nested
    @DisplayName("findById method")
    class FindByIdMethod {

        @Test
        @DisplayName("when called and item is persisted, then it should return it")
        void whenCalled_thenItShouldReturnTheStore() {
            Store store = new Store(0, "Store", "Address", "123456789", 0L, 0L);
            Store savedStore = jdbcStoreRepository.save(store);

            assertTrue(jdbcStoreRepository.findById(savedStore.getId()).isPresent());
        }

        @Test
        @DisplayName("when called and item is not persisted, then it should return an empty optional")
        void whenCalled_thenItShouldReturnAnEmptyOptional() {
            assertTrue(jdbcStoreRepository.findById(1).isEmpty());
        }
    }
}