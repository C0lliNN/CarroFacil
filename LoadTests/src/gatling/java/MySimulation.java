import com.github.javafaker.Faker;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class MySimulation extends Simulation {
    Faker faker = new Faker();
    // Feed email
    Iterator<Map<String, Object>> feeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String name = faker.name().fullName();
                        String email = faker.internet().safeEmailAddress();
                        String password = faker.internet().password();

                        return Map.of("name", name, "email", email, "password", password);
                    }
            ).iterator();

    ScenarioBuilder scn = scenario("scn")
            .feed(feeder)
            .exec(http("My Request").post("/auth/register")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"#{name}\", \"email\": \"#{email}\", \"password\": \"#{password}\"}"))
                    .check(status().is(201)));

    HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8080");

    {
        setUp(
                // generate a closed workload injection profile
                // with each level incrementing 10 users
                // each level lasting 10 seconds
                /// it should get stop at 1000 users
                scn.injectClosed(
                        incrementConcurrentUsers(10)
                                .times(5)
                                .eachLevelLasting(Duration.ofSeconds(10))
                                .separatedByRampsLasting(Duration.ofSeconds(10))
                                .startingFrom(0))
        ).protocols(httpProtocol);
    }

}
