package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.User;
import com.raihan.nexaorder.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataUserRepository extends JpaRepository<User, Long> {

    List<User> findAllByOrderByCreatedAtDesc();

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);
}

