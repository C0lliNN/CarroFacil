package com.raphaelcollin.inventorymanagement;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.raphaelcollin.inventorymanagement.infrastructure.web.filter.AuthorizationFilter;
import com.raphaelcollin.inventorymanagement.infrastructure.web.filter.FilterChainExceptionHandler;
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
import org.testcontainers.containers.PostgreSQLContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@EntityScan(basePackages = "com.raphaelcollin.inventorymanagement.*")
@WireMockTest(httpPort = 8787)
@ActiveProfiles("test")
public class IntegrationTest {
    private static final String IMAGE = "postgres:16";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(IMAGE)
            .withExposedPorts(5432)
            .withEnv("POSTGRES_USER", "postgres")
            .withEnv("POSTGRES_PASSWORD", "postgres")
            .withEnv("POSTGRES_DB", "postgres");


    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    private AuthorizationFilter authorizationFilter;

    @Autowired
    private FilterChainExceptionHandler filterChainExceptionHandler;

    protected MockMvc mockMvc;

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                //.addFilter(authorizationFilter)
                //.addFilter(filterChainExceptionHandler)
                .build();

        stubFor(
                post(urlEqualTo("/auth/validate"))
                        .withRequestBody(equalToJson("{\"token\":\"valid\"}"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{\"id\":\"b1bb55cc-0645-49a6-b077-104c48e1ffc2\",\"name\":\"Raphael Collin\",\"type\":\"CUSTOMER\",\"email\":\"test\"}")
                        )
        );

        stubFor(
                post(urlEqualTo("/auth/validate"))
                        .withRequestBody(equalToJson("{\"token\":\"invalid\"}"))
                        .willReturn(
                                aResponse()
                                        .withStatus(401)
                                        .withBody("{\"message\":\"invalid_token\"}")
                        )
        );
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM vehicles");
        jdbcTemplate.update("DELETE FROM vehicle_types");
        jdbcTemplate.update("DELETE FROM stores");
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        container.start();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("auth.authorizationserver.url", () -> "http://localhost:8787/auth/validate");
    }
}
