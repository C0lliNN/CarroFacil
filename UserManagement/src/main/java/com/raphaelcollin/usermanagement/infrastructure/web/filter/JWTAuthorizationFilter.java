package com.raphaelcollin.usermanagement.infrastructure.web.filter;

import com.raphaelcollin.usermanagement.core.User;
import com.raphaelcollin.usermanagement.core.exception.InvalidTokenException;
import com.raphaelcollin.usermanagement.infrastructure.web.TokenExtractor;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final TokenExtractor tokenExtractor;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        return uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/docs") ||
                uri.startsWith("/swagger-ui") ||
                uri.startsWith("/participant") ||
                uri.startsWith("/auth");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);
            User user = tokenExtractor.extractUserFromToken(token);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw e;
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER)).
                filter(header -> header.startsWith(PREFIX)).
                map(header -> header.replace(PREFIX, ""))
                .orElseThrow(() -> new InvalidTokenException("The 'Authorization' header must be in the following format: 'Bearer token'"));
    }
}
