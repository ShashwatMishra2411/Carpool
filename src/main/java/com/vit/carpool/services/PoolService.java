package com.vit.carpool.services;

import com.vit.carpool.entities.Combined;
import com.vit.carpool.entities.Pool;
import com.vit.carpool.entities.User;
import com.vit.carpool.mapper.UserRowMapper;
import com.vit.carpool.mapper.CombinedRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.vit.carpool.exceptions.*;

@Service
public class PoolService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Combined> getAllPools() {
        try {
            String query = "SELECT p.*, u.registrationnumber AS creator_registrationnumber, u.name AS creator_name " +
                    "FROM pool p " +
                    "LEFT JOIN users u ON p.creatorID = u.registrationnumber";
            return namedParameterJdbcTemplate.query(query, new CombinedRowMapper());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve pools: " + e.getMessage());
        }
    }

    public Combined getPoolById(long poolID) {
        try {
            String query = "SELECT p.*, u.registrationnumber AS creator_registrationnumber, u.name AS creator_name " +
                    "FROM pool p " +
                    "LEFT JOIN users u ON p.creatorID = u.registrationnumber " +
                    "WHERE p.poolID = :poolID";
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("poolID", poolID);
            return namedParameterJdbcTemplate.queryForObject(query, params, new CombinedRowMapper());
        } catch (Exception e) {
            throw new PoolNotFoundException("Pool with ID " + poolID + " not found");
        }
    }

    public List<Combined> getPoolByUserId(long userid) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("creatorid", userid);
        String query = "SELECT p.*, u.registrationnumber AS creator_registrationnumber, u.name AS creator_name " +
                "FROM pool p " +
                "LEFT JOIN users u ON p.creatorID = u.registrationnumber " +
                "WHERE p.creatorid = :creatorid";
        return namedParameterJdbcTemplate.query(query, params, new CombinedRowMapper());
    }

    @Transactional
    public int createPool(Pool pool) {
        try {
            // Fetching user details
            String userQuery = "SELECT * FROM users WHERE registrationnumber = :registrationnumber";
            MapSqlParameterSource params1 = new MapSqlParameterSource()
                    .addValue("registrationnumber", pool.getCreator().getRegistrationNumber());

            User user = namedParameterJdbcTemplate.queryForObject(userQuery, params1, new UserRowMapper());

            pool.setCreator(user);
            if (user == null) {
                throw new UserPrincipalNotFoundException("User Not Found");
            }
            ArrayList<Long> createdPools = user.getCreatedPools();

            // Check for pool limit
            if (createdPools.size() >= 5) {
                throw new PoolLimitExceededException("A user can only create up to 5 pools.");
            }

            // Check for users length vs fill value
            if (pool.getUsers().length != pool.getFill()) {
                throw new InvalidPoolDataException("Length of users array must match fill column value");
            }

            // Insert pool into the pool table and retrieve the generated poolID
            String insertQuery = "INSERT INTO pool (source, destination, date, time, creatorID, max_users, fill, users) "
                    + "VALUES ( :source, :destination, :date, :time, :creatorID, :max_users, :fill, :users) "
                    + "RETURNING poolid";
            ArrayList<String> userList = new ArrayList<>(Arrays.asList(pool.getUsers()));

            userList.add(pool.getCreator().getRegistrationNumber());
            System.out.println(userList);
            pool.getUsers();
            MapSqlParameterSource insertParams = new MapSqlParameterSource()
                    .addValue("source", pool.getSource())
                    .addValue("destination", pool.getDestination())
                    .addValue("date", pool.getDate())
                    .addValue("time", pool.getTime())
                    .addValue("creatorID", pool.getCreator().getRegistrationNumber())
                    .addValue("max_users", pool.getMaxUsers())
                    .addValue("fill", 1)
                    .addValue("users", userList.toArray(new String[0]), Types.ARRAY);

            List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(insertQuery, insertParams);
            System.out.println(result);
            // Retrieve the generated poolID
            Long generatedPoolID = (Long) result.get(0).get("poolid");
            System.out.println("pooldID = " + generatedPoolID);
            // Set the generated poolID to the pool object
            pool.setPoolID(generatedPoolID);

            // Add the generated poolID to the user's created pools list
            createdPools.add(generatedPoolID);

            // Update the user's created pools list with the new poolID
            String updateQuery = "UPDATE users SET createdpools = :createdPools WHERE registrationnumber = :registrationnumber";
            MapSqlParameterSource updateParams = new MapSqlParameterSource()
                    .addValue("createdPools", createdPools.toArray(new Long[0]))
                    .addValue("registrationnumber", user.getRegistrationNumber());

            return namedParameterJdbcTemplate.update(updateQuery, updateParams);

        } catch (PoolLimitExceededException | InvalidPoolDataException e) {
            throw e; // Re-throw custom exceptions
        } catch (Exception e) {
            throw new RuntimeException("Failed to create pool: " + e.getMessage());
        }
    }

    @Transactional
    public int updatePool(long poolID, Pool pool) {
        if (pool.getUsers().length != pool.getFill()) {
            throw new InvalidPoolDataException("Length of users array must match fill column value");
        }
        if (pool.getMaxUsers() < pool.getFill()) {
            throw new InvalidPoolDataException("Max users should not be less than current number of users");
        }
        try {
            String query = "UPDATE pool SET source = :source, destination = :destination, date = :date, time = :time, "
                    +
                    "creatorID = :creatorID, max_users = :max_users, fill = :fill, users = :users WHERE poolID = :poolID";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("source", pool.getSource())
                    .addValue("destination", pool.getDestination())
                    .addValue("date", pool.getDate())
                    .addValue("time", pool.getTime())
                    .addValue("creatorID", pool.getCreator().getRegistrationNumber())
                    .addValue("max_users", pool.getMaxUsers())
                    .addValue("fill", pool.getFill())
                    .addValue("users", pool.getUsers())
                    .addValue("poolID", poolID);

            return namedParameterJdbcTemplate.update(query, params);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update pool with ID " + poolID + ": " + e.getMessage());
        }
    }

    @Transactional
    public int deletePool(long poolID) {
        try {
            // Remove poolID from createdpools in users table
            String updateUsersQuery = "UPDATE users " +
                    "SET createdpools = array_remove(createdpools, :poolID) " +
                    "WHERE :poolID = ANY(createdpools)";
            MapSqlParameterSource updateParams = new MapSqlParameterSource().addValue("poolID", poolID);
            namedParameterJdbcTemplate.update(updateUsersQuery, updateParams);

            // Delete the pool
            String deletePoolQuery = "DELETE FROM pool WHERE poolID = :poolID";
            return namedParameterJdbcTemplate.update(deletePoolQuery, updateParams);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete pool with ID " + poolID + ": " + e.getMessage());
        }
    }

    @Transactional
    public int removeUserFromPool(long poolId, String userId) {
        try {
            System.out.println(poolId + " " + userId);
            String query = "UPDATE pool " +
                    "SET users = array_remove(users, :userId), fill = fill - 1 " +
                    "WHERE poolid = :poolId AND :userId = ANY(users)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("poolId", poolId);

            int rowsAffected = namedParameterJdbcTemplate.update(query, params);
            if (rowsAffected == 0) {
                throw new RuntimeException("User not found in the pool or invalid pool ID.");
            }
            return rowsAffected;
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove user from pool: " + e.getMessage());
        }
    }
}
