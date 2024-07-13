package com.raphaelcollin.usermanagement.infrastructure.persistence;

import com.raphaelcollin.usermanagement.core.User;
import com.raphaelcollin.usermanagement.core.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DynamoDBUserRepository implements UserRepository {
    @Value("${dynamodb.table.name}")
    private String tableName;

    private final DynamoDbClient client;

    @Override
    public Optional<User> findById(String id) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.builder().s(id).build()))
                .build();

        GetItemResponse response = client.getItem(request);
        if (response == null || response.item().isEmpty()) {
            return Optional.empty();
        } else {
            Map<String, AttributeValue> item = response.item();
            return mapRecordToUser(item);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("emailIndex")
                .keyConditionExpression("email = :email")
                .expressionAttributeValues(Map.of(":email", AttributeValue.builder().s(email).build()))
                .build();

        QueryResponse response = client.query(request);
        if (response == null || response.items().isEmpty()) {
            return Optional.empty();
        } else {
            Map<String, AttributeValue> item = response.items().get(0);
            return mapRecordToUser(item);
        }
    }

    private Optional<User> mapRecordToUser(Map<String, AttributeValue> item) {
        return Optional.of(new User(
                item.get("id").s(),
                item.get("name").s(),
                item.get("type").s(),
                item.get("email").s(),
                item.get("password").s()
        ));
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(Map.of(
                        "id", AttributeValue.builder().s(user.getId()).build(),
                        "name", AttributeValue.builder().s(user.getName()).build(),
                        "email", AttributeValue.builder().s(user.getEmail()).build(),
                        "type", AttributeValue.builder().s(user.getType()).build(),
                        "password", AttributeValue.builder().s(user.getPassword()).build()
                ))
                .build();

        client.putItem(request);

        return user;
    }
}
