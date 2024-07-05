package com.raphaelcollin.usermanagement.infrastructure.initialization;

import com.raphaelcollin.usermanagement.core.PasswordEncoder;
import com.raphaelcollin.usermanagement.core.User;
import com.raphaelcollin.usermanagement.core.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class EmployeeInitialization implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${employee.email}")
    private String email;

    @Value("${employee.password}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> {
                        },
                        () -> {
                            User employee = User.builder()
                                    .name("Employee")
                                    .email(email)
                                    .password(passwordEncoder.hashPassword(password))
                                    .type(User.Type.EMPLOYEE)
                                    .build();

                            userRepository.save(employee);
                        });
    }
}
