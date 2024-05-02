package com.accio.api.service;

import com.accio.api.entity.user.User;
import com.accio.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User getUser(Principal principal) {
        if (principal == null) throw new ResponseStatusException(NOT_ACCEPTABLE, "user is null");
        return userRepository.findById(Integer.parseInt(principal.getName()))
                .orElseThrow(() -> new ResponseStatusException(NOT_ACCEPTABLE, "user not found"));
    }
}
