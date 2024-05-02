package com.accio.api.controller;

import com.accio.api.controller.request.CreateCustomerRequest;
import com.accio.api.controller.request.LoginRequest;
import com.accio.api.response.TokenResponse;
import com.accio.api.service.LoginService;
import com.accio.api.service.SignUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Auth", description = "Operations to manage `Auth` operations")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthController {
    private final LoginService loginService;
    private final SignUpService signUpService;

    @PostMapping("login")
    @Operation(description = "create a new user")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @PostMapping("signup")
    @Operation(description = "student signup")
    public Integer signUp(@RequestBody @Valid CreateCustomerRequest request) {
        return signUpService.signUp(request);
    }

    @PostMapping("initialAdmin")
    @Operation(description = "create initial admin")
    public void createInitialAdmin(@RequestBody CreateCustomerRequest request) {
        signUpService.createInitialAdmin(request);
    }
}
