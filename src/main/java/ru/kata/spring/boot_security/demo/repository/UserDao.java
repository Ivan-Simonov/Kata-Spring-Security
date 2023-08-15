package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    void save(User user);
    List<User> findAll();
    void deleteById(Long id);
}
