package com.raphaelcollin.inventorymanagement.infrastructure.config;

import com.raphaelcollin.inventorymanagement.infrastructure.web.filter.AuthorizationFilter;
import com.raphaelcollin.inventorymanagement.infrastructure.web.filter.FilterChainExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
class WebSecurityConfig {
    private final AuthorizationFilter authorizationFilter;
    private final FilterChainExceptionHandler filterChainExceptionHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);
        http.addFilterAfter(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(request -> {
            request.requestMatchers("/v3/api-docs/**", "/docs/**", "/swagger-ui/**", "/actuator/**").permitAll();
            request.anyRequest().authenticated();
        });

        return http.build();
    }
}
