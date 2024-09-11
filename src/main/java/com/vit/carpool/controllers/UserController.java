package com.vit.carpool.controllers;

import com.vit.carpool.entities.User;
import com.vit.carpool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody User user) {
        try {
            String jwtToken = userService.signInUser(user);
            return ResponseEntity.ok("JWT Token: " + jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
