package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Initialization {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;

    private List<Role> roles = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    @Autowired
    public Initialization(UserServiceImpl userService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUsers() {
        roles = roleService.getAllRoles();
        users = userService.getAllUsers();

        if(roles.isEmpty()) {
            roles = createRoles();
            roleService.saveAll(roles);
        }

        if(users.isEmpty()) {
            users = createUsers();
            userService.saveAll(users);
        }


    }

    private List<Role> createRoles() {
        List<Role> roleList = new ArrayList<>();
        Role adminRole = new Role("ROLE_ADMIN");
        roleList.add(adminRole);
        Role userRole = new Role("ROLE_USER");
        roleList.add(userRole);
        return roleList;
    }

    private List<User> createUsers() {
        List<User> userList = new ArrayList<>();

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setFirstName("Mr");
        admin.setLastName("Gigachad");
        admin.setRoles(roles.stream().filter(r -> r.getName().equals("ROLE_ADMIN")).collect(Collectors.toList()));
        admin.getRoles().stream().forEach(r -> r.setUsers(List.of(admin)));
        userList.add(admin);

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setFirstName("Noname");
        user.setLastName("Whocares");
        user.setRoles(roles.stream().filter(r -> r.getName().equals("ROLE_USER")).collect(Collectors.toList()));
        user.getRoles().stream().forEach(r -> r.setUsers(List.of(user)));
        userList.add(user);

        return userList;
    }
}
