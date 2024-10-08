package com.raphaelcollin.usermanagement.infrastructure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@EntityScan(basePackages = "com.raphaelcollin.usermanagement.*")
@ActiveProfiles("test")
public class IntegrationTest {
    private static final String IMAGE = "localstack/localstack:3.4.0";

    protected static final GenericContainer<?> container = new GenericContainer<>(IMAGE)
            .withExposedPorts(4566);

    @Autowired
    protected DynamoDbClient dynamoDbClient;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        setupDynamoDB();
    }

    private void setupDynamoDB() {
        dynamoDbClient.createTable(r -> r.tableName("users")
                .attributeDefinitions(
                        AttributeDefinition.builder().attributeName("id").attributeType("S").build(),
                        AttributeDefinition.builder().attributeName("email").attributeType("S").build()
                )
                .keySchema(
                        KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build()
                )
                .globalSecondaryIndexes(
                        GlobalSecondaryIndex.builder()
                                .indexName("emailIndex")
                                .keySchema(
                                        KeySchemaElement.builder().attributeName("email").keyType(KeyType.HASH).build()
                                )
                                .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(1L).writeCapacityUnits(1L).build())
                                .build()
                )
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(1L).writeCapacityUnits(1L).build())
        );
    }


    @AfterEach
    void tearDown() {
        dynamoDbClient.deleteTable(r -> r.tableName("users"));
    }

    @DynamicPropertySource
    static void localstackProperties(DynamicPropertyRegistry registry) {
        container.start();
        registry.add("spring.cloud.aws.dynamodb.endpoint", () -> "http://localhost:" + container.getMappedPort(4566));
        registry.add("spring.cloud.aws.sqs.endpoint", () -> "http://localhost:" + container.getMappedPort(4566));

    }
}
