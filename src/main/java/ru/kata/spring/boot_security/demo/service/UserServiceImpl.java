package ru.kata.spring.boot_security.demo.service;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveAll(List<User> users) {
        userRepository.saveAllAndFlush(users);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalIdentifierException(String.format("No user found for id = %s", id)));
    }

    public void update(Long id, User user) {
        User userStored = userRepository.findById(id).orElse(user);
        userStored.setFirstName(user.getFirstName());
        userStored.setLastName(user.getLastName());
        userStored.setUsername(user.getUsername());
        userStored.setRoles(user.getRoles());
        if (!userStored.getPassword().equals(user.getPassword())) {
            userStored.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userStored);
    }

    public void createNewUser(User user) {
        if (user.getRoles().isEmpty()) {
            Role role = roleRepository.findByName("ROLE_USER");
            user.setRoles(List.of(role));
        }
        save(user);
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }
}
