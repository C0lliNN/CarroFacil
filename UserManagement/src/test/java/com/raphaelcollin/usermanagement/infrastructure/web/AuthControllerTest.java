package com.raphaelcollin.usermanagement.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphaelcollin.usermanagement.core.PasswordEncoder;
import com.raphaelcollin.usermanagement.core.User;
import com.raphaelcollin.usermanagement.core.UserRepository;
import com.raphaelcollin.usermanagement.core.request.LoginRequest;
import com.raphaelcollin.usermanagement.core.request.RegisterRequest;
import com.raphaelcollin.usermanagement.infrastructure.IntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest extends IntegrationTest {
    private static final String BASE_URL = "/auth";
    private final String NAME = "Raphael";
    private final String EMAIL = "test_auth2@test.com";
    private final String PASSWORD = "password";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        user = User.builder()
                .name(NAME)
                .type(User.Type.CUSTOMER)
                .email(EMAIL)
                .password(passwordEncoder.hashPassword(PASSWORD))
                .build();
    }

    @Nested
    @DisplayName("method: login(LoginRequest)")
    class LoginMethod{

        @BeforeEach
        void setUp() {
            user = repository.save(user);
        }

        @Test
        @DisplayName("when called without email, then it should return 400 error")
        void whenCalledWithoutEmail_shouldReturn400Error() throws Exception {
            LoginRequest payload = new LoginRequest(null, PASSWORD);

            MockHttpServletRequestBuilder request = post(BASE_URL + "/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("email"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called with valid email, then it should return 400 error")
        void whenCalledWithValidEmail_shouldReturn400Error() throws Exception {
            LoginRequest payload = new LoginRequest("test.com", PASSWORD);

            MockHttpServletRequestBuilder request = post(BASE_URL + "/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("email"))
                    .andExpect(jsonPath("$.details[0].message").value("the field must be a valid email"));
        }

        @Test
        @DisplayName("when called without password, then it should return 400 error")
        void whenCalledWithoutPassword_shouldReturn400Error() throws Exception {
            LoginRequest payload = new LoginRequest(EMAIL, null);

            MockHttpServletRequestBuilder request = post(BASE_URL + "/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("password"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called with unknown email, then it should 401 error")
        void whenCalledWithUnknownEmail_shouldReturn401Error() throws Exception {
            LoginRequest payload = new LoginRequest("test5@test.com", PASSWORD);

            MockHttpServletRequestBuilder request = post(BASE_URL + "/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("The email '%s' could not be found.".formatted(payload.email())))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when called with incorrect password, then it should 401 error")
        void whenCalledWithIncorrectPassword_shouldReturn401Error() throws Exception {
            LoginRequest payload = new LoginRequest(EMAIL, "password2");

            MockHttpServletRequestBuilder request = post(BASE_URL + "/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("The provided password is incorrect."))
                    .andExpect(jsonPath("$.details").isEmpty());
        }

        @Test
        @DisplayName("when called with valid credentials, then it should 200 and the token")
        void whenCalledWithValidCredentials_shouldReturn200AndTheToken() throws Exception {
            LoginRequest payload = new LoginRequest(EMAIL, PASSWORD);

            MockHttpServletRequestBuilder request = post(BASE_URL + "/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(user.getId()))
                    .andExpect(jsonPath("$.name").value(user.getName()))
                    .andExpect(jsonPath("$.email").value(user.getEmail()))
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }
    }

    @Nested
    @DisplayName("method: register(RegisterRequest)")
    class RegisterMethod {

        @Test
        @DisplayName("when called without name, then it should return 400 error")
        void whenCalledWithoutName_shouldReturn400Error() throws Exception {
            RegisterRequest payload = new RegisterRequest(
                    null,
                    EMAIL,
                    PASSWORD
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("name"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called with invalid name, then it should return 400 error")
        void whenCalledWithInvalidName_shouldReturn400Error() throws Exception {
            RegisterRequest payload = new RegisterRequest(
                    "a".repeat(151),
                    EMAIL,
                    PASSWORD
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("name"))
                    .andExpect(jsonPath("$.details[0].message").value("the field must contain at most 150 chars"));
        }

        @Test
        @DisplayName("when called without email, then it should return 400 error")
        void whenCalledWithoutEmail_shouldReturn400Error() throws Exception {
            RegisterRequest payload = new RegisterRequest(
                    NAME,
                    "",
                    PASSWORD
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("email"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called with invalid email, then it should return 400 error")
        void whenCalledWithInvalidEmail_shouldReturn400Error() throws Exception {
            RegisterRequest payload = new RegisterRequest(
                    NAME,
                    "test.com",
                    PASSWORD
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("email"))
                    .andExpect(jsonPath("$.details[0].message").value("the field must be a valid email"));
        }

        @Test
        @DisplayName("when called without password, then it should return 400 error")
        void whenCalledWithoutPassword_shouldReturn400Error() throws Exception {
            RegisterRequest payload = new RegisterRequest(
                    NAME,
                    EMAIL,
                    null
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("password"))
                    .andExpect(jsonPath("$.details[0].message").value("the field is mandatory"));
        }

        @Test
        @DisplayName("when called with invalid password, then it should return 400 error")
        void whenCalledWithInvalidPassword_shouldReturn400Error() throws Exception {
            RegisterRequest payload = new RegisterRequest(
                    NAME,
                    EMAIL,
                    "test"
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("The given payload is invalid. Check the 'details' field."))
                    .andExpect(jsonPath("$.details[0].field").value("password"))
                    .andExpect(jsonPath("$.details[0].message").value("the field must contain between 6 and 20 chars"));
        }

        @Test
        @DisplayName("when called valid data, then it should return 201 and persist the user")
        void whenCalledWithValidData_shouldReturn201AndPersistTheUser() throws Exception {
            RegisterRequest payload = new RegisterRequest(
                    NAME,
                    EMAIL,
                    PASSWORD
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name").value(payload.name()))
                    .andExpect(jsonPath("$.email").value(payload.email()))
                    .andExpect(jsonPath("$.token").isNotEmpty());

            assertThat(repository.findByEmail(payload.email())).isNotEmpty();
        }

        @Test
        @DisplayName("when called with existing email, then it should return 409 error")
        void whenCalledWithExistingEmail_shouldReturn409Error() throws Exception {
            User user = User.builder()
                    .name(NAME)
                    .type(User.Type.CUSTOMER)
                    .email(EMAIL)
                    .password(PASSWORD)
                    .build();

            repository.save(user);

            RegisterRequest payload = new RegisterRequest(
                    "Raphael",
                    user.getEmail(),
                    "password"
            );

            MockHttpServletRequestBuilder request = post(BASE_URL + "/register")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload));

            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("The email '%s' is already in use.".formatted(payload.email())))
                    .andExpect(jsonPath("$.details").isEmpty());
        }
    }
}