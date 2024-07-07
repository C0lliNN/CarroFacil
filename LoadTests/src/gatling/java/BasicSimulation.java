import com.github.javafaker.Faker;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class BasicSimulation extends Simulation {
    private final String AUTH_BASE_URL = "token";
    private final String INVENTORY_BASE_URL = "http://localhost:8081";
    private final String BOOKING_BASE_URL = "http://localhost:8082";
    private final String EMPLOYEE_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiRW1wbG95ZWUiLCJpZCI6ImM1YmMxNzgzLTlkZDAtNGNkMS04ZDgyLTVkMDhiMTBjNGE1MiIsInR5cGUiOiJFTVBMT1lFRSIsImVtYWlsIjoiZW1wbG95ZWVAdGVzdC5jb20iLCJpYXQiOjE3MjAzNzY1MzgsImV4cCI6MTcyMDQ2MjkzOH0.7Wka0hlLdOM243L14CEfhGN7ICGFqcYrgkEkcm5ADcAcqb8TLcrhisZxcPwD8uZItgnuBgP3GezPTRuqwbmjxw";

    Faker faker = new Faker();

    Set<String> emails = Collections.synchronizedSet(new HashSet<>());
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

    ScenarioBuilder registerScenario = scenario("register")
            .feed(feeder)
            .exec(
                    http("Register")
                        .post(AUTH_BASE_URL + "/register")
                        .header("Content-Type", "application/json")
                        .body(StringBody("{\"name\": \"#{name}\", \"email\": \"#{email}\", \"password\": \"#{password}\"}"))
                    .check(status().is(201))
                    .check(jsonPath("$.token").saveAs("token")))
            .exec(http("Create Vehicle")
                    .put(INVENTORY_BASE_URL + "/vehicles")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + EMPLOYEE_TOKEN)
                    .body(StringBody("{\"typeId\":1,\"storeId\":1,\"make\":\"Hyundai\",\"model\":\"Creta\",\"year\":2024,\"mileage\":10000,\"licensePlate\":\"23525\",\"chassisNumber\":\"252355125\",\"engineNumber\":\"234242\",\"color\":\"white\"}"))
                    .check(status().is(200))
                    .check(jsonPath("$.id").saveAs("vehicleId")))
            .exec(http("Book Vehicle")
                    .post(BOOKING_BASE_URL + "/bookings")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer #{token}")
                    .body(StringBody("{\"vehicleId\":#{vehicleId},\"startTime\":\"2024-12-03T10:15:30\",\"endTime\":\"2024-12-04T10:15:30\"}"))
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("bookingId")))
            .exec(http("Check in Booking")
                    .patch(BOOKING_BASE_URL + "/bookings/#{bookingId}/check-in")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + EMPLOYEE_TOKEN)
                    .check(status().is(200)))
            .exec(http("Check out Booking")
                    .patch(BOOKING_BASE_URL + "/bookings/#{bookingId}/check-out")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + EMPLOYEE_TOKEN)
                    .check(status().is(200)));


    HttpProtocolBuilder httpProtocol =
            http.enableHttp2();

    {
        setUp(
                registerScenario.injectClosed(
                        incrementConcurrentUsers(10)
                                .times(2)
                                .eachLevelLasting(Duration.ofSeconds(10))
                                .separatedByRampsLasting(Duration.ofSeconds(5))
                                .startingFrom(0))
        ).protocols(httpProtocol);
    }

}
