package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void save(User user);
    List<User> getAllUsers();
    void delete(Long id);
    User findById(Long id);
    void update(Long id, User user);
    void createNewUser(User user);
    User findUserByEmail(String email);
}
