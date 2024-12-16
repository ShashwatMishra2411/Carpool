package com.vit.carpool.mapper;

import com.vit.carpool.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Array;
import java.util.ArrayList;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setRegistrationNumber(rs.getString("registrationnumber"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setGender(rs.getString("gender"));
        user.setJoined(0l);
        // Retrieve the SQL Array from the ResultSet
        Array sqlArray = rs.getArray("createdPools");
        if (sqlArray != null) {
            // Convert the SQL Array to a Long[]
            Long[] poolIds = (Long[]) sqlArray.getArray();

            // Convert the Long[] to an ArrayList<Pool>
            ArrayList<Long> createdPools = new ArrayList<>();
            for (Long poolId : poolIds) {
                createdPools.add(poolId);
            }

            // Set the createdPools in the User object
            user.setCreatedPools(createdPools);
        }

        return user;
    }
}