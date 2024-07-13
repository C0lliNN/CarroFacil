package com.raphaelcollin.customerservice.infrastructure.persistence;

import com.raphaelcollin.customerservice.core.Customer;
import com.raphaelcollin.customerservice.core.service.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class DynamoDBCustomerRepository implements CustomerRepository {
    @Value("${dynamodb.table.name}")
    private String tableName;

    private final DynamoDbClient client;

    @Override
    public Optional<Customer> findByUserId(String userId) {
        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("userIdIndex")
                .keyConditionExpression("user_id = :userId")
                .expressionAttributeValues(Map.of(":userId", AttributeValue.builder().s(userId).build()))
                .build();

        QueryResponse response = client.query(request);
        if (response == null || response.items().isEmpty()) {
            return Optional.empty();
        } else {
            Map<String, AttributeValue> item = response.items().get(0);
            return mapRecordToCustomer(item);
        }
    }

    private Optional<Customer> mapRecordToCustomer(Map<String, AttributeValue> item) {
        return Optional.of(new Customer(
                item.get("id").s(),
                item.get("name").s(),
                item.get("user_id").s(),
                item.get("bookings_count") == null ? 0 : Integer.parseInt(item.get("bookings_count").n())
        ));
    }

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null || customer.getId().isEmpty()) {
            customer.setId(UUID.randomUUID().toString());
        }

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(Map.of(
                        "id", AttributeValue.builder().s(customer.getId()).build(),
                        "name", AttributeValue.builder().s(customer.getName()).build(),
                        "user_id", AttributeValue.builder().s(customer.getUserId()).build(),
                        "bookings_count", AttributeValue.builder().n(String.valueOf(customer.getBookingsCount())).build()
                ))
                .build();

        client.putItem(request);

        return customer;
    }
}
