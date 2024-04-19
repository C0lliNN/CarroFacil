package com.raphaelcollin.usermanagement.infrastructure;

import com.raphaelcollin.usermanagement.core.User;
import com.raphaelcollin.usermanagement.core.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DynamoDBUserRepository implements UserRepository {
    @Value("${dynamodb.table.name}")
    private String tableName;

    private final DynamoDbClient client;

    @Override
    public Optional<User> findByEmail(String email) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("email", AttributeValue.builder().s(email).build()))
                .build();

        GetItemResponse response = client.getItem(request);
        if (!response.hasItem()) {
            return Optional.empty();
        } else {
            Map<String, AttributeValue> item = response.item();
            return Optional.of(new User(
                    item.get("id").s(),
                    item.get("name").s(),
                    item.get("email").s(),
                    item.get("password").s()
            ));
        }
    }

    @Override
    public User save(User user) {
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(Map.of(
                        "id", AttributeValue.builder().s(user.getId()).build(),
                        "name", AttributeValue.builder().s(user.getName()).build(),
                        "email", AttributeValue.builder().s(user.getEmail()).build(),
                        "password", AttributeValue.builder().s(user.getPassword()).build()
                ))
                .build();

        client.putItem(request);

        return user;
    }
}
