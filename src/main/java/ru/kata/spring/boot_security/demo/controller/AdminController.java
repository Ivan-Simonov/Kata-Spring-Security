package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private static final String ADMIN_REDIRECT_ADDRESS = "redirect:/admin";

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String printUsers(Principal principal, ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("currentUser", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "admin/admin";
    }

    @PostMapping()
    public String create(@ModelAttribute @Valid User user, Errors errors, ModelMap modelMap) {
        if(errors.hasErrors()) {
            modelMap.addAttribute("roles", roleService.getAllRoles());
            return "admin/new";
        }
        userService.createNewUser(user);
        return ADMIN_REDIRECT_ADDRESS;
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("updatedUser") @Valid User user, Errors errors, @PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return "redirect:" + request.getHeader("Referer");
        }
        userService.update(id, user);
        return ADMIN_REDIRECT_ADDRESS;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ADMIN_REDIRECT_ADDRESS;
    }
}
