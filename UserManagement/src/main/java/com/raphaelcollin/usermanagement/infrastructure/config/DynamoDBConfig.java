package com.raphaelcollin.usermanagement.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.endpoints.Endpoint;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbClient dynamoDbClient(
            @Value("${aws.region}") String region, @Value("${spring.cloud.aws.dynamodb.endpoint:}") String endpoint) {
        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .region(Region.of(region));

        if (!endpoint.isEmpty()) {
            builder.endpointOverride(Endpoint.builder().url(URI.create(endpoint)).build().url());
        }
        return builder.build();
    }
}
