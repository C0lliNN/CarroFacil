package com.raphaelcollin.bookingservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@EntityScan(basePackages = "com.raphaelcollin.inventorymanagement.*")
@ActiveProfiles("test")
public class IntegrationTest {
    private static final String POSTGRES_IMAGE = "postgres:16";

    protected static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withExposedPorts(5432)
            .withEnv("POSTGRES_USER", "postgres")
            .withEnv("POSTGRES_PASSWORD", "postgres")
            .withEnv("POSTGRES_DB", "postgres");

    private static final String LOCAL_STACK_IMAGE = "localstack/localstack:3.4.0";

    protected static final GenericContainer<?> localstackContainer = new GenericContainer<>(LOCAL_STACK_IMAGE)
            .withExposedPorts(4566);


    @Autowired
    private SnsClient snsClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    private String topicArn;

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        CreateTopicResponse response = snsClient.createTopic(r -> r.name("booking-events"));
        this.topicArn = response.topicArn();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM bookings");
        snsClient.deleteTopic(r -> r.topicArn(topicArn));
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        postgresContainer.start();
        localstackContainer.start();
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.cloud.aws.sns.endpoint", () -> "http://localhost:" + localstackContainer.getMappedPort(4566));
    }
}
