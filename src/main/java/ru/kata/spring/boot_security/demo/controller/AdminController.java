package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> allUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/info")
    public User admin(Principal principal) {
        return userService.findUserByEmail(principal.getName());
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable long id) {
        return userService.findById(id);
    }

    @GetMapping("/roles")
    public List<Role> roles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/add")
    public void addUser(@RequestBody User user) {
        userService.createNewUser(user);
    }

    @PatchMapping("/update")
    public void updateUser(@RequestBody User user) {
        userService.update(user.getId(), user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.delete(id);
    }

}
