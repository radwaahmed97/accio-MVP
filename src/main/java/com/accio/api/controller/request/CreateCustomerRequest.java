package com.accio.api.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class CreateCustomerRequest {
    @NotBlank(message = "full name is required")
    private String fullName;
    @Email
    private String email;
    @NotBlank(message = "password is required")
    private String password;
    @Pattern(regexp = "^\\+?\\d{8,}$", message = "the phone filed should be well-formed e.g.{1234567890,+11234567890}")
    private String phone;

    public void normalize() {
        setEmail(getEmail().trim());
        setFullName(getFullName().trim());
        if (phone != null) {
            phone = phone.trim();
        }
    }
}
