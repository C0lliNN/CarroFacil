package com.raphaelcollin.inventorymanagement.infrastructure.web.filter;

import com.raphaelcollin.inventorymanagement.core.exception.InvalidTokenException;
import com.raphaelcollin.inventorymanagement.infrastructure.web.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationFilterTest {

    @Nested
    @DisplayName("shouldNotFilter method")
    class ShouldNotFilterMethod {

        @ParameterizedTest
        @CsvSource({
                "/v3/api-docs,true",
                "/docs,true",
                "/swagger-ui,true",
                "/actuator/health,true",
                "/vehicles,false",
                "/stores,false"
        })
        @DisplayName("when uri is whitelisted, then it should return true")
        void testShouldNotFilter(String uri, boolean expected) {
            var filter = new AuthorizationFilter(null);
            var request = new MockHttpServletRequest(new MockServletContext());
            request.setRequestURI(uri);

            var result = filter.shouldNotFilter(request);

            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("doFilterInternal method")
    class DoFilterInternalMethod {

        @Test
        @DisplayName("when token is valid, then it should call the next filter in the chain")
        void testDoFilterInternal() {
            var authorizationClient = new AuthorizationClient() {
                @Override
                public User validateToken(String token) {
                    return new User("1", "username", "CUSTOMER", "email", "token");
                }
            };
            var filter = new AuthorizationFilter(authorizationClient);
            var request = new MockHttpServletRequest(new MockServletContext());
            request.addHeader("Authorization", "Bearer token");
            var response = new MockHttpServletResponse();
            var chain = new MockFilterChain();

            assertDoesNotThrow(() -> filter.doFilterInternal(request, response, chain));
        }

        @Test
        @DisplayName("when token is invalid, then it should throw an exception")
        void testDoFilterInternalInvalidToken() {
            var authorizationClient = new AuthorizationClient() {
                @Override
                public User validateToken(String token) {
                    throw new InvalidTokenException("Invalid token");
                }
            };
            var filter = new AuthorizationFilter(authorizationClient);
            var request = new MockHttpServletRequest(new MockServletContext());
            var response = new MockHttpServletResponse();
            var chain = new MockFilterChain();

            assertThrows(InvalidTokenException.class, () -> filter.doFilterInternal(request, response, chain));
        }
    }

}