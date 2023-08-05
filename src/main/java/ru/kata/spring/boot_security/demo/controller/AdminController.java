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
    public String printUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/{id}")
    public String printUser(@PathVariable("id") Long id, ModelMap modelMap) {
        modelMap.addAttribute("user", userService.findById(id));
        return "/admin/user";
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

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, ModelMap modelMap) {
        modelMap.addAttribute("roles", roleService.getAllRoles());
        return "admin/new";
    }

    @GetMapping("/{id}/edit")
    public String editUser(ModelMap modelMap, @PathVariable("id") Long id) {
        modelMap.addAttribute("user", userService.findById(id));
        modelMap.addAttribute("roles", roleService.getAllRoles());
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, Errors errors, @PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
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
