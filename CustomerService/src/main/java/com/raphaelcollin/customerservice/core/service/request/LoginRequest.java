package com.raphaelcollin.customerservice.core.service.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "the field is mandatory")
        @Email(message = "the field must be a valid email")
        String email,

        @NotBlank(message = "the field is mandatory")
        String password) {
}
