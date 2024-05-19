package com.raphaelcollin.usermanagement.infrastructure.config;

import io.awspring.cloud.autoconfigure.core.CredentialsProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.endpoints.Endpoint;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbClient dynamoDbClient(
            @Value("${spring.cloud.aws.dynamodb.region}") String region,
            @Value("${spring.cloud.aws.credentials.access-key}") String accessKey,
            @Value("${spring.cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${spring.cloud.aws.dynamodb.endpoint:}") String endpoint) {

        CredentialsProperties credentialsProperties = new CredentialsProperties();
        credentialsProperties.setAccessKey(accessKey);
        credentialsProperties.setSecretKey(secretKey);

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                credentialsProperties.getAccessKey(),
                credentialsProperties.getSecretKey()
        );

        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(() -> awsBasicCredentials);

        if (!endpoint.isEmpty()) {
            builder.endpointOverride(Endpoint.builder().url(URI.create(endpoint)).build().url());
        }

        return builder.build();
    }
}
