package com.raphaelcollin.employeeservice.infrastructure.persistence;

import com.raphaelcollin.employeeservice.core.Employee;
import com.raphaelcollin.employeeservice.core.service.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class DynamoDBEmployeeRepository implements EmployeeRepository {
    @Value("${dynamodb.table.name}")
    private String tableName;

    private final DynamoDbClient client;

    @Override
    public Optional<Employee> findByUserId(String userId) {
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
            return mapRecordToEmployee(item);
        }
    }

    private Optional<Employee> mapRecordToEmployee(Map<String, AttributeValue> item) {
        return Optional.of(new Employee(
                item.get("id").s(),
                item.get("name").s(),
                item.get("user_id").s()
        ));
    }

    @Override
    public Employee save(Employee customer) {
        if (customer.getId() == null || customer.getId().isEmpty()) {
            customer.setId(UUID.randomUUID().toString());
        }

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(Map.of(
                        "id", AttributeValue.builder().s(customer.getId()).build(),
                        "name", AttributeValue.builder().s(customer.getName()).build(),
                        "user_id", AttributeValue.builder().s(customer.getUserId()).build()
                ))
                .build();

        client.putItem(request);

        return customer;
    }
}
