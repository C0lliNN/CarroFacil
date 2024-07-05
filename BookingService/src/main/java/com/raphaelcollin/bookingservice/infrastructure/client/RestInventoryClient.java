package com.raphaelcollin.bookingservice.infrastructure.client;

import com.raphaelcollin.bookingservice.core.Vehicle;
import com.raphaelcollin.bookingservice.core.exception.EntityNotFoundException;
import com.raphaelcollin.bookingservice.core.exception.InventoryBookingException;
import com.raphaelcollin.bookingservice.core.service.InventoryClient;
import com.raphaelcollin.bookingservice.infrastructure.web.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestInventoryClient implements InventoryClient {
    private final RestTemplate restTemplate;

    @Value("${inventorymanagement.client.baseurl}")
    private String baseUrl;

    @Override
    public Vehicle getVehicle(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());
        HttpEntity<VehicleResponse> entity = new HttpEntity<>(headers);

        ResponseEntity<VehicleResponse> response = restTemplate.exchange(baseUrl + "/vehicles/{id}", HttpMethod.GET, entity, VehicleResponse.class, id);
        if (response.getStatusCode().value() == 404) {
            throw new EntityNotFoundException("Vehicle not found");
        }

        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Error while fetching vehicle");
        }

        return response.getBody().toVehicle();
    }

    @Override
    public void book(int vehicleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(baseUrl + "/vehicles/{id}/book", HttpMethod.PATCH, entity, Void.class, vehicleId);
        } catch (Exception e) {
            throw new InventoryBookingException("Error while booking vehicle: " + e.getMessage());
        }
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.token();
    }
}
