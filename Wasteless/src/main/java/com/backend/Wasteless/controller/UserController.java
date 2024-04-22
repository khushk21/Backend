package com.backend.Wasteless.controller;

import com.backend.Wasteless.model.User;
import com.backend.Wasteless.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registerUser")
    public String createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        if (savedUser != null) {
            return "Successfully saved " + savedUser.getUserName();
        } else {
            return "Failed to save user";
        }
    }

    @PostMapping("/userLogin")
    public HashMap<String, Object> verifyCredentials(@RequestBody User credentials) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            User existingUser = userService.verifyCredentials(credentials).orElse(null);
            result.put("user", existingUser);
            result.put("error", null);
        } catch (Exception e) {
            result.put("user", null);
            result.put("error", new Error(e.getMessage()));
        }
        return result;
    }

    @GetMapping("/getAllUser")
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/getUser")
    public Optional<User> getUser(@RequestParam String userName) {
        return userService.getUserByUsername(userName);
    }
}
