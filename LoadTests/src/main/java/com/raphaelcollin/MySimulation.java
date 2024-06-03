package com.raphaelcollin;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class MySimulation extends Simulation {
    ScenarioBuilder scn = scenario("scn")
            .exec(http("My Request").post("/auth/register")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Raphael Collin\", \"email\": \"raphael@test.com\", \"password\": \"password\"}"))
                    .check(status().is(201)));

    HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8080");

    {
        setUp(
                scn.injectOpen(atOnceUsers(5)).protocols(httpProtocol)
        ).protocols(httpProtocol);
    }

}
