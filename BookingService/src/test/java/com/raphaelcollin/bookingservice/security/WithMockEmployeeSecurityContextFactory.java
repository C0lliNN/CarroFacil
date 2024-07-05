package com.raphaelcollin.bookingservice.security;

import com.raphaelcollin.bookingservice.infrastructure.web.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockEmployeeSecurityContextFactory implements WithSecurityContextFactory<WithMockEmployee> {
    @Override
    public SecurityContext createSecurityContext(WithMockEmployee annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = new User("some-id", annotation.username(), "EMPLOYEE", "email", "token");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        context.setAuthentication(auth);
        return context;
    }
}
