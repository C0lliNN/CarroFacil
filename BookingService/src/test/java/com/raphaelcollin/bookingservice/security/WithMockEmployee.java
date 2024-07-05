package com.raphaelcollin.bookingservice.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockEmployeeSecurityContextFactory.class)
public @interface WithMockEmployee {

    String username() default "rob";

    String name() default "Rob Winch";
}