import com.github.javafaker.Faker;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class MySimulation extends Simulation {
    Faker faker = new Faker();

    Set<String> emails = new HashSet<>();
    Lock lock = new ReentrantLock();

    // Feed email
    Iterator<Map<String, Object>> feeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        lock.lock();
                        String email;
                        while (true) {
                            email = faker.internet().safeEmailAddress();
                            if (emails.add(email)) {
                                lock.unlock();
                                break;
                            }
                        }

                        String name = faker.name().fullName();
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
            http.baseUrl("http://user-management-alb-1628517429.us-east-1.elb.amazonaws.com");

    {
        setUp(
                scn.injectClosed(
                        incrementConcurrentUsers(10)
                                .times(25)
                                .eachLevelLasting(Duration.ofSeconds(10))
                                .separatedByRampsLasting(Duration.ofSeconds(10))
                                .startingFrom(0))
        ).protocols(httpProtocol);
    }

}
