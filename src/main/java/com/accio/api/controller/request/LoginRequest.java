package com.accio.api.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
  @Email(message = "Invalid email address format")
  private String email;
  @NotBlank(message = "Password is mandatory")
  @Size(min = 5, max = 100, message = "Invalid Password Format")
  //TODO; this error message appear in the fronend like [Invalid credentials], please remove these brackets
  private String password;
}
