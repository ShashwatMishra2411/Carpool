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

import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public int createPool(Pool pool) {
        try {
            String userQuery = "SELECT * FROM users WHERE registrationnumber = :registrationnumber";
            MapSqlParameterSource params1 = new MapSqlParameterSource()
                    .addValue("registrationnumber", pool.getCreator().getRegistrationNumber());

            User user = namedParameterJdbcTemplate.queryForObject(userQuery, params1, new UserRowMapper());

            pool.setCreator(user);
            ArrayList<Long> createdPools = user.getCreatedPools();
            if (createdPools.size() >= 5) {
                throw new PoolLimitExceededException("A user can only create up to 5 pools.");
            }
            if (pool.getUsers().length != pool.getFill()) {
                throw new InvalidPoolDataException("Length of users array must match fill column value");
            }

            String insertQuery = "INSERT INTO pool (poolid, source, destination, date, time, creatorID, max_users, fill, users) "
                    +
                    "VALUES (:poolid, :source, :destination, :date, :time, :creatorID, :max_users, :fill, :users)";
            MapSqlParameterSource insertParams = new MapSqlParameterSource()
                    .addValue("poolid", pool.getPoolID())
                    .addValue("source", pool.getSource())
                    .addValue("destination", pool.getDestination())
                    .addValue("date", pool.getDate())
                    .addValue("time", pool.getTime())
                    .addValue("creatorID", pool.getCreator().getRegistrationNumber())
                    .addValue("max_users", pool.getMaxUsers())
                    .addValue("fill", pool.getFill())
                    .addValue("users", pool.getUsers());

            namedParameterJdbcTemplate.update(insertQuery, insertParams);

            createdPools.add(pool.getPoolID());
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
            String query = "DELETE FROM pool WHERE poolID = :poolID";
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("poolID", poolID);
            return namedParameterJdbcTemplate.update(query, params);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete pool with ID " + poolID + ": " + e.getMessage());
        }
    }

    // Custom RowMapper for Combined entity
    // private static class CombinedRowMapper implements RowMapper<Combined> {
    // @Override
    // public Combined mapRow(ResultSet rs, int rowNum) throws SQLException {
    // Combined combined = new Combined();
    // combined.setPoolID(rs.getLong("poolID"));
    // combined.setSource(rs.getString("source"));
    // combined.setDestination(rs.getString("destination"));
    // combined.setDate(rs.getDate("date").toLocalDate());
    // combined.setTime(rs.getTime("time").toLocalTime());
    // combined.setMaxUsers(rs.getInt("max_users"));
    // combined.setFill(rs.getInt("fill"));
    // combined.setCreatorID(rs.getString("creatorID"));

    // // Handle conversion from PgArray to String[]
    // Array usersArray = rs.getArray("users");
    // if (usersArray != null) {
    // combined.setUsers((String[]) usersArray.getArray());
    // } else {
    // combined.setUsers(new String[0]);
    // }

    // combined.setCreatorName(rs.getString("creator_name"));
    // return combined;
    // }
    // }
}
