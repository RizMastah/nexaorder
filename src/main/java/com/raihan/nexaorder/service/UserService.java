package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUsers();
}
