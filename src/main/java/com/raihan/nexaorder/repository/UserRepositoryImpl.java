package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.User;
import com.raihan.nexaorder.enums.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SpringDataUserRepository delegate;

    public UserRepositoryImpl(SpringDataUserRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public User save(User user) {
        return delegate.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    public List<User> findAllByOrderByCreatedAtDesc() {
        return delegate.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return delegate.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return delegate.existsByEmail(email);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return delegate.findByRole(role);
    }
}
