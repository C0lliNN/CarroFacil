package com.raphaelcollin.usermanagement.infrastructure.web;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@EntityScan(basePackages = "com.raphaelcollin.usermanagement.*")
@ActiveProfiles("test")
public class AbstractE2ETest {
    private static final String IMAGE = "localstack/localstack:3.4.0";

    static final GenericContainer<?> container = new GenericContainer<>(IMAGE)
            .withExposedPorts(4566);

    @DynamicPropertySource
    static void localstackProperties(DynamicPropertyRegistry registry) {
        container.start();
        registry.add("spring.cloud.aws.dynamodb.endpoint", () -> "http://localhost:" + container.getMappedPort(4566));
        registry.add("spring.cloud.aws.sqs.endpoint",  () -> "http://localhost:" + container.getMappedPort(4566));

    }
}
