package com.vit.carpool.services;

import com.vit.carpool.entities.Combined;
import com.vit.carpool.entities.Pool;
import com.vit.carpool.entities.User;
import com.vit.carpool.mapper.UserRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PoolService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private User user = new User();

    // Method to retrieve all pool entries
    public List<Combined> getAllPools() {
        String query = "SELECT p.*, u.registrationnumber AS creator_registrationnumber, u.name AS creator_name " +
                "FROM pool p " +
                "LEFT JOIN users u ON p.creatorID = u.registrationnumber";
        return namedParameterJdbcTemplate.query(query, new CombinedRowMapper());
    }

    // Method to find a pool by id
    public Combined getPoolById(long poolID) {
        String query = "SELECT p.*, u.registrationnumber AS creator_registrationnumber, u.name AS creator_name " +
                "FROM pool p " +
                "LEFT JOIN users u ON p.creatorID = u.registrationnumber " +
                "WHERE p.poolID = :poolID";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("poolID", poolID);
        return namedParameterJdbcTemplate.queryForObject(query, params, new CombinedRowMapper());
    }

    // Method to create a new pool
    @Transactional
    public int createPool(Pool pool) {
        String query1 = "Select * from users where registrationnumber= :registrationnumber";
        MapSqlParameterSource params1 = new MapSqlParameterSource();
        params1.addValue("registrationnumber", pool.getCreator().getRegistrationNumber());
        User user = (namedParameterJdbcTemplate.queryForObject(query1, params1, new UserRowMapper()));

        pool.setCreator(user);
        System.out.println(pool.getCreator().getCreatedPools());

        if (pool.getCreator().getCreatedPools().size() >= 5) {
            throw new IllegalStateException("A user can only create up to 5 pools.");
        }
        if (pool.getUsers().length != pool.getFill()) {
            throw new IllegalArgumentException("Length of users array must be equal to fill column value");
        }

        ArrayList<Long> createdPools = pool.getCreator().getCreatedPools();
        createdPools.add(pool.getPoolID());
        String query = "INSERT INTO pool (source, destination, date, time, creatorID, max_users, fill, users) " +
                "VALUES (:source, :destination, :date, :time, :creatorID, :max_users, :fill, :users)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("source", pool.getSource());
        params.addValue("destination", pool.getDestination());
        params.addValue("date", pool.getDate());
        params.addValue("time", pool.getTime());
        params.addValue("creatorID", pool.getCreator().getRegistrationNumber()); // Foreign key reference to User
        params.addValue("max_users", pool.getMaxUsers());
        params.addValue("fill", pool.getFill());
        params.addValue("users", pool.getUsers());
        return namedParameterJdbcTemplate.update(query, params);
    }

    // Method to update a pool
    @Transactional
    public int updatePool(long poolID, Pool pool) {
        if (pool.getUsers().length != pool.getFill()) {
            throw new IllegalArgumentException("Length of users array must be equal to fill column value");
        }

        String query = "UPDATE pool SET source = :source, destination = :destination, date = :date, time = :time, " +
                "creatorID = :creatorID, max_users = :max_users, fill = :fill, users = :users WHERE poolID = :poolID";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("source", pool.getSource());
        params.addValue("destination", pool.getDestination());
        params.addValue("date", pool.getDate());
        params.addValue("time", pool.getTime());
        params.addValue("creatorID", pool.getCreator().getRegistrationNumber()); // Foreign key reference to User
        params.addValue("max_users", pool.getMaxUsers());
        params.addValue("fill", pool.getFill());
        params.addValue("users", pool.getUsers());
        params.addValue("poolID", poolID);
        return namedParameterJdbcTemplate.update(query, params);
    }

    // Method to delete a pool
    @Transactional
    public int deletePool(long poolID) {
        String query = "DELETE FROM pool WHERE poolID = :poolID";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("poolID", poolID);
        return namedParameterJdbcTemplate.update(query, params);
    }

    // Custom RowMapper for Combined entity
    private static class CombinedRowMapper implements RowMapper<Combined> {
        @Override
        public Combined mapRow(ResultSet rs, int rowNum) throws SQLException {
            Combined combined = new Combined();
            combined.setPoolID(rs.getLong("poolID"));
            combined.setSource(rs.getString("source"));
            combined.setDestination(rs.getString("destination"));
            combined.setDate(rs.getDate("date").toLocalDate());
            combined.setTime(rs.getTime("time").toLocalTime());
            combined.setMaxUsers(rs.getInt("max_users"));
            combined.setFill(rs.getInt("fill"));
            combined.setCreatorID(rs.getString("creatorID"));

            // Handle conversion from PgArray to String[]
            Array usersArray = rs.getArray("users");
            if (usersArray != null) {
                combined.setUsers((String[]) usersArray.getArray());
            } else {
                combined.setUsers(new String[0]);
            }

            combined.setCreatorName(rs.getString("creator_name"));
            return combined;
        }
    }
}