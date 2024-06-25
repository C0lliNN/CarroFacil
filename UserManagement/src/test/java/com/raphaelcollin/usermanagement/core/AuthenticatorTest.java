package com.raphaelcollin.usermanagement.core;


import com.raphaelcollin.usermanagement.core.exception.DuplicateEmailException;
import com.raphaelcollin.usermanagement.core.exception.EmailNotFoundException;
import com.raphaelcollin.usermanagement.core.exception.IncorrectPasswordException;
import com.raphaelcollin.usermanagement.core.request.LoginRequest;
import com.raphaelcollin.usermanagement.core.request.RegisterRequest;
import com.raphaelcollin.usermanagement.core.response.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticatorTest {

    @InjectMocks
    private Authenticator useCase;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenManager tokenManager;

    @Nested
    @DisplayName("method: login(LoginRequest)")
    class LoginMethod {

        @AfterEach
        void tearDown() {
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("when user is not found, then it should throw an EmailNotFoundException")
        void whenUserIsNotFound_shouldThrowAnEmailNotFoundException() {
            LoginRequest request = new LoginRequest("some-email", "some-password");

            when(repository.findByEmail(request.email())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.login(request))
                    .isInstanceOf(EmailNotFoundException.class)
                    .hasMessage("The email '%s' could not be found.", request.email());

            verify(repository).findByEmail(request.email());
            verifyNoInteractions(passwordEncoder, tokenManager);
        }

        @Test
        @DisplayName("when password is incorrect, then it should throw an IncorrectPasswordException")
        void whenPasswordIsIncorrect_shouldThrowAnIncorrectPasswordException() {
            LoginRequest request = new LoginRequest("some-email", "some-password");

            User user = User.builder()
                    .id("some-id")
                    .name("some-name")
                    .type(User.Type.CUSTOMER)
                    .email(request.email())
                    .password("some-hashed-password")
                    .build();

            when(repository.findByEmail(request.email())).thenReturn(Optional.of(user));
            when(passwordEncoder.comparePasswordAndHash(request.password(), user.getPassword())).thenReturn(false);

            assertThatThrownBy(() -> useCase.login(request))
                    .isInstanceOf(IncorrectPasswordException.class)
                    .hasMessage("The provided password is incorrect.");


            verify(repository).findByEmail(request.email());
            verify(passwordEncoder).comparePasswordAndHash(request.password(), user.getPassword());

            verifyNoMoreInteractions(passwordEncoder);
            verifyNoInteractions(tokenManager);
        }

        @Test
        @DisplayName("when password is correct, then it should return a UserResponse")
        void whenPasswordIsCorrect_shouldReturnAUserResponse() {
            LoginRequest request = new LoginRequest("some-email", "some-password");
            User user = User.builder().
                    id("some-id")
                    .name("some-name")
                    .type(User.Type.CUSTOMER)
                    .email(request.email())
                    .password("some-hashed-password")
                    .build();

            String token = "some token";

            when(repository.findByEmail(request.email())).thenReturn(Optional.of(user));
            when(passwordEncoder.comparePasswordAndHash(request.password(), user.getPassword())).thenReturn(true);
            when(tokenManager.generateTokenForUser(user)).thenReturn(token);

            UserResponse expected = UserResponse.fromUser(user).withToken(token);
            UserResponse actual = useCase.login(request);

            assertThat(actual).isEqualTo(expected);

            verify(repository).findByEmail(request.email());
            verify(passwordEncoder).comparePasswordAndHash(request.password(), user.getPassword());
            verify(tokenManager).generateTokenForUser(user);

            verifyNoMoreInteractions(passwordEncoder, tokenManager);
        }
    }

    @Nested
    @DisplayName("method: register(RegisterRequest)")
    class RegisterMethod {

        @Test
        @DisplayName("when called and the email is already in use, then it should throw a DuplicateEmailException")
        void whenCalledAndTheEmailIsAlreadyInUse_shouldThrowADuplicateEmailException() {
            RegisterRequest request = new RegisterRequest("some-name", "some-email", "some-password");
            User existingUser = User.builder()
                    .id("some-id")
                    .name("some-name")
                    .type(User.Type.CUSTOMER)
                    .email(request.email())
                    .password("some-hashed-password")
                    .build();

            when(repository.findByEmail(request.email())).thenReturn(Optional.of(existingUser));

            assertThatThrownBy(() -> useCase.register(request))
                    .isInstanceOf(DuplicateEmailException.class)
                    .hasMessage("The email '%s' is already in use.", existingUser.getEmail());

            verify(repository).findByEmail(request.email());
            verifyNoInteractions(passwordEncoder, tokenManager);
        }

        @Test
        @DisplayName("when called, then it should register a new user")
        void whenCalled_shouldRegisterANewUser() {
            RegisterRequest request = new RegisterRequest("some-name", "some-email", "some-password");
            User user = request.toUser();
            user.setPassword("some-hashed-password");

            when(passwordEncoder.hashPassword(request.password())).thenReturn("some-hashed-password");
            when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
            when(repository.save(user)).thenReturn(user);
            when(tokenManager.generateTokenForUser(user)).thenReturn("new-token");

            UserResponse expected = UserResponse.fromUser(user).withToken("new-token");
            UserResponse actual = useCase.register(request);

            assertThat(actual).isEqualTo(expected);

            verify(passwordEncoder).hashPassword(request.password());
            verify(repository).save(user);
            verify(tokenManager).generateTokenForUser(user);

            verifyNoMoreInteractions(passwordEncoder, repository, tokenManager);
        }
    }
}