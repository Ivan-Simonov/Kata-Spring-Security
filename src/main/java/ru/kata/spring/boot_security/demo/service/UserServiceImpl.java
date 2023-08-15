package ru.kata.spring.boot_security.demo.service;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleDao;
import ru.kata.spring.boot_security.demo.repository.UserDao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void delete(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userDao.findById(id).orElseThrow(() -> new IllegalIdentifierException(String.format("No user found for id = %s", id)));
    }

    @Override
    public void update(Long id, User user) {
        User userStored = userDao.findById(id).orElse(user);
        userStored.setFirstName(user.getFirstName());
        userStored.setLastName(user.getLastName());
        userStored.setEmail(user.getEmail());
        userStored.setRoles(user.getRoles());
        if (!userStored.getPassword().equals(user.getPassword())) {
            userStored.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        reloadRoles(userStored);
        userDao.save(userStored);
    }

    @Override
    public void createNewUser(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Optional<Role> role = roleDao.findByName("ROLE_USER");
            if (role.isEmpty()) {
                throw new RuntimeException("Incorrectly filled database with missing role: ROLE_USER");
            } else {
                user.setRoles(Set.of(role.get()));
            }
        } else {
            reloadRoles(user);
        }
        save(user);
    }

    @Override
    public User findUserByEmail(String email) throws UsernameNotFoundException {
        return userDao.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", email)));
    }

    private void reloadRoles(User user) {
        List<Role> roles = user.getRoles().stream().map(role -> roleDao.findById(Long.valueOf(role.getName())).get()).toList();
        user.getRoles().clear();
        user.getRoles().addAll(roles);
    }
}
