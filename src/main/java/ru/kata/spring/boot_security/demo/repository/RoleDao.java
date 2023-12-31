package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao {
    Optional<Role> findByName(String name);
    List<Role> findAll();
    Optional<Role> findById(Long id);
}
