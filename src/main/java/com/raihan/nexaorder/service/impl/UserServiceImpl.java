package com.raihan.nexaorder.service.impl;

import com.raihan.nexaorder.dto.UserResponseDTO;
import com.raihan.nexaorder.repository.UserRepository;
import com.raihan.nexaorder.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }
}
