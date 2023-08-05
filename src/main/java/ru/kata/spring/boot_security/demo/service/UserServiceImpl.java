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
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalIdentifierException(String.format("No user found for id = %s", id)));
    }

    @Override
    public void update(Long id, User user) {
        User userStored = userRepository.findById(id).orElse(user);
        userStored.setFirstName(user.getFirstName());
        userStored.setLastName(user.getLastName());
        userStored.setEmail(user.getEmail());
        userStored.setRoles(user.getRoles());
        if (!userStored.getPassword().equals(user.getPassword())) {
            userStored.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userStored);
    }

    @Override
    public void createNewUser(User user) {
        if (user.getRoles().isEmpty()) {
            Role role = roleRepository.findByName("ROLE_USER");
            user.setRoles(Set.of(role));
        }
        save(user);
    }

    @Override
    public User findUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", email)));
    }
}
