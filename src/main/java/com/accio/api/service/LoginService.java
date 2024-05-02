package com.accio.api.service;

import com.accio.api.controller.request.LoginRequest;
import com.accio.api.repository.user.UserRepository;
import com.accio.api.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private static final int LONG_TOKEN_MINUTES = (int) TimeUnit.MINUTES.convert(12, TimeUnit.HOURS);
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginRequest request) {
        var userOptional = userRepository.findByEmailIgnoreCase(request.getEmail());
        try {
            return userOptional.map(user -> {
                if (!user.isVerified()) {
                    throw new ResponseStatusException(BAD_REQUEST, "the user should be verified");
                }
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                var token = jwtService.createToken(user, LONG_TOKEN_MINUTES);
                return new TokenResponse(token, Instant.now().plus(LONG_TOKEN_MINUTES, MINUTES));
            }).orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Invalid credentials"));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid credentials");
        }
    }
}