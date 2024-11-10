package com.vit.carpool.services;

import com.vit.carpool.entities.User;
import com.vit.carpool.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    // Method to handle user sign-in or registration and return a JWT token
    @Transactional
    public String signInUser(User user) {
        try {
            user.setRegistrationNumber(user.getRegistrationNumber().toLowerCase());
            String query = "SELECT * FROM users WHERE registrationnumber = :registrationNumber";

            // Find if the user already exists in the database
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("registrationNumber", user.getRegistrationNumber());

            Optional<User> existingUser = namedParameterJdbcTemplate
                    .query(query, params, new BeanPropertyRowMapper<>(User.class)).stream().findFirst();

            if (existingUser.isPresent()) {
                // If the user exists, return a JWT token
                return jwtUtil.generateToken(existingUser.get().getRegistrationNumber());
            } else {
                // Insert a new user into the database
                String insertQuery = "INSERT INTO users (registrationnumber, name, gender, email) VALUES (:registrationNumber, :name, :gender, :email)";
                params.addValue("name", user.getName());
                params.addValue("gender", user.getGender());
                params.addValue("email", user.getEmail());

                int rowsAffected = namedParameterJdbcTemplate.update(insertQuery, params);

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
}