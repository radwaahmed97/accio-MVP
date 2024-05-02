package com.accio.api.service;

import com.accio.api.controller.request.CreateCustomerRequest;
import com.accio.api.entity.user.Admin;
import com.accio.api.entity.user.Customer;
import com.accio.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Integer signUp(CreateCustomerRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ResponseStatusException(BAD_REQUEST, "this user already exists");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new ResponseStatusException(BAD_REQUEST, "this phone number already exists");
        }
        Customer customer = new Customer();
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setFullName(request.getFullName());
        customer.setVerified(true);
        userRepository.save(customer);
        return customer.getId();
    }

    public void createInitialAdmin(CreateCustomerRequest request) {
        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setPhone(request.getPhone());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setFullName(request.getFullName());
        admin.setVerified(true);
        userRepository.save(admin);
    }
}
