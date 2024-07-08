import com.github.javafaker.Faker;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class BasicSimulation extends Simulation {
    private final String AUTH_BASE_URL = "http://user-management-alb-1338286536.us-east-1.elb.amazonaws.com/auth";
    private final String INVENTORY_BASE_URL = "http://inventory-management-alb-849344746.us-east-1.elb.amazonaws.com";
    private final String BOOKING_BASE_URL = "http://booking-service-alb-1157582990.us-east-1.elb.amazonaws.com";
    private final String EMPLOYEE_TOKEN = "token";

    private final int NUM_USERS = 100;
    private final int RAMP_UP_TIME = 30;

    // 14:39

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
                        rampConcurrentUsers(0).to(NUM_USERS).during(Duration.ofMinutes(RAMP_UP_TIME))
                )
        ).protocols(httpProtocol);
    }

}
