package com.raphaelcollin.usermanagement.infrastructure.web.exception;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExceptionBodyTest {

    @Nested
    @DisplayName("method: fromMessage(string)")
    class FromMessageMethod {

        @Test
        @DisplayName("when called, then it should create the object with a message and empty details")
        void whenCalled_shouldCreateTheObjectWithAMessageAndEmptyDetails() {
            String message = "something went wrong";

            ExceptionBody exceptionBody = ExceptionBody.fromMessage(message);

            assertThat(exceptionBody.getMessage()).isEqualTo(message);
            assertThat(exceptionBody.getDetails()).isEmpty();
        }
    }

    @Nested
    @DisplayName("method: fromException(Throwable)")
    class FromExceptionThrowableMethod {

        @Test
        @DisplayName("when called, then it should create the object with a message and empty details")
        void whenCalled_shouldCreateTheObjectWithAMessageAndEmptyDetails() {
            String message = "something went wrong";
            var exception = new RuntimeException(message);

            ExceptionBody exceptionBody = ExceptionBody.fromException(exception);

            assertThat(exceptionBody.getMessage()).isEqualTo(message);
            assertThat(exceptionBody.getDetails()).isEmpty();
        }
    }

    @Nested
    @DisplayName("method: fromException(MethodArgumentNotValidException)")
    class FromExceptionMethod {
        @Mock
        private MethodArgumentNotValidException methodArgumentNotValidException;

        @Mock
        private FieldError fieldError1;

        @Mock
        private FieldError fieldError2;

        @AfterEach
        void tearDown() {
            verify(methodArgumentNotValidException).getFieldErrors();
            verifyNoMoreInteractions(methodArgumentNotValidException);
        }

        @Test
        @DisplayName("when exception does not contain field errors, then it should DTO with empty 'errors' field")
        void whenExceptionDoesNotContainFieldErrors_shouldReturnDtoWithEmptyDetailsField() {
            when(methodArgumentNotValidException.getFieldErrors()).thenReturn(Collections.emptyList());

            ExceptionBody exceptionBody = ExceptionBody.fromException(methodArgumentNotValidException);

            assertThat(exceptionBody).isNotNull();
            assertThat(exceptionBody.getMessage()).isEqualTo("The given payload is invalid. Check the 'details' field.");
            assertThat(exceptionBody.getDetails()).isEmpty();
        }

        @Test
        @DisplayName("when exception contains field errors, then it should DTO with 'details' field")
        void whenExceptionContainsFieldErrors_shouldReturnDtoWithDetailsField() {
            when(fieldError1.getField()).thenReturn("field1");
            when(fieldError1.getDefaultMessage()).thenReturn("field is mandatory");

            when(fieldError2.getField()).thenReturn("aField");
            when(fieldError2.getDefaultMessage()).thenReturn("field is mandatory");

            ExceptionBody.ErrorDetail expectedErrorDetail1 = new ExceptionBody.ErrorDetail("aField", "field is mandatory");
            ExceptionBody.ErrorDetail expectedErrorDetail2 = new ExceptionBody.ErrorDetail("field1", "field is mandatory");

            when(methodArgumentNotValidException.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

            final ExceptionBody exceptionBody = ExceptionBody.fromException(methodArgumentNotValidException);

            verify(fieldError1, times(2)).getField();
            verify(fieldError1).getDefaultMessage();
            verify(fieldError2, times(2)).getField();
            verify(fieldError2).getDefaultMessage();

            verifyNoMoreInteractions(fieldError1);
            verifyNoMoreInteractions(fieldError2);

            assertThat(exceptionBody).isNotNull();
            assertThat(exceptionBody.getMessage()).isEqualTo("The given payload is invalid. Check the 'details' field.");
            assertThat(exceptionBody.getDetails()).containsExactly(expectedErrorDetail1, expectedErrorDetail2);
        }
    }
}