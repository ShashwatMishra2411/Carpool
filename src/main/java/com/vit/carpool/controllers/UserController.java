package com.vit.carpool.controllers;

import com.vit.carpool.entities.User;
import com.vit.carpool.services.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    User user = new User();

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            // Fetch the user details
            User user = userService.getUser(id);

            // Query to check pools the user is a member of
            String checkQuery = "SELECT poolid FROM pool WHERE :userId = ANY(users);";
            MapSqlParameterSource checkParams = new MapSqlParameterSource().addValue("userId", id);

            // Fetch the pool IDs the user is part of
            List<Long> results = namedParameterJdbcTemplate.queryForList(checkQuery, checkParams, Long.class);

            // Fetch pools the user has created
            List<Long> createdPools = user.getCreatedPools();

            // Check for pools the user has joined but not created
            for (long poolId : results) {
                System.out.println("Pools the user is a member of: " + results);
                if (!createdPools.contains(poolId)) {
                    user.setJoined(poolId); // Assuming this method updates joined pools
                }
            }

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error and return a meaningful response
            System.err.println("Error fetching user: " + e.getMessage());
            return ResponseEntity.status(500).body("Error fetching user: " + e.getMessage());
        }
    }

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
