package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.LoginRequestDTO;
import com.raihan.nexaorder.dto.RegisterRequestDTO;
import com.raihan.nexaorder.entity.User;

public interface AuthService {

    User registerCustomer(RegisterRequestDTO request);

    User login(LoginRequestDTO request);
}
