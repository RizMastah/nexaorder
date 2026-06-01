package com.raihan.nexaorder.service.impl;

import com.raihan.nexaorder.dto.LoginRequestDTO;
import com.raihan.nexaorder.dto.RegisterRequestDTO;
import com.raihan.nexaorder.entity.User;
import com.raihan.nexaorder.enums.UserRole;
import com.raihan.nexaorder.repository.UserRepository;
import com.raihan.nexaorder.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User registerCustomer(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password confirmation does not match");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(UserRole.CUSTOMER)
                .build();

        return userRepository.save(user);
    }

    @Override
    public User login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }
}
