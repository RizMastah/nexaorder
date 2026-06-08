package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.User;
import com.raihan.nexaorder.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAllByOrderByCreatedAtDesc();

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);
}
