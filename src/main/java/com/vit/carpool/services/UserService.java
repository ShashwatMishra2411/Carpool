package com.vit.carpool.services;

import com.vit.carpool.entities.User;
import com.vit.carpool.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    // Method to handle user sign-in or registration and return a JWT token
    public String signInUser(User user) {
        try {
            String query = "SELECT * FROM users WHERE registrationnumber = ?";

            // Find if the user already exists in the database
            Optional<User> existingUser = jdbcTemplate.query(query, new Object[]{user.getRegistrationNumber()}, this::mapRowToUser)
                    .stream().findFirst();
            System.out.println(existingUser.isPresent());
            if (existingUser.isPresent()) {
                // If the user exists, return a JWT token
                return jwtUtil.generateToken(existingUser.get().getRegistrationNumber());
            } else {
                // Insert a new user into the database
                String insertQuery = "INSERT INTO users (registrationnumber, name) VALUES (?, ?)";
                int rowsAffected = jdbcTemplate.update(insertQuery, user.getRegistrationNumber(), user.getName());

                if (rowsAffected > 0) {
                    // Return a JWT token for the newly created user
                    return jwtUtil.generateToken(user.getRegistrationNumber());
                } else {
                    throw new Exception("Failed to insert the user into the database");
                }
            }
        } catch (Exception e) {
            // Handle error and return appropriate message
            return "Error during sign-in: " + e.getMessage();
        }
    }

    // Method to map the result set row to a User object
    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setRegistrationNumber(rs.getString("registrationnumber"));
        user.setName(rs.getString("name"));
        return user;
    }
}
