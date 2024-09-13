package com.vit.carpool.services;

import com.vit.carpool.entities.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PoolService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Method to retrieve all pool entries
    public List<Pool> getAllPools() {
        String query = "SELECT * FROM pool";
        return namedParameterJdbcTemplate.query(query, new BeanPropertyRowMapper<>(Pool.class));
    }

    // Method to find a pool by id
    public Pool getPoolById(long poolID) {
        String query = "SELECT * FROM pool WHERE poolID = :poolID";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("poolID", poolID);
        return namedParameterJdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(Pool.class));
    }

    // Method to create a new pool
    @Transactional
    public int createPool(Pool pool) {
        String query = "INSERT INTO pool (poolID, source, destination, date, time, creatorID, max_users, fill) " +
                "VALUES (:poolID, :source, :destination, :date, :time, :creatorID, :max_users, :fill)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("poolID", pool.getPoolID());
        params.addValue("source", pool.getSource());
        params.addValue("destination", pool.getDestination());
        params.addValue("date", pool.getDate());
        params.addValue("time", pool.getTime());
        params.addValue("creatorID", pool.getCreatorID().getRegistrationNumber()); // Foreign key reference to User
        params.addValue("max_users", pool.getmax_users());
        params.addValue("fill", pool.getFill());
        return namedParameterJdbcTemplate.update(query, params);
    }

    // Method to update a pool
    @Transactional
    public int updatePool(long poolID, Pool pool) {
        String query = "UPDATE pool SET source = :source, destination = :destination, date = :date, time = :time, " +
                "creatorID = :creatorID, max_users = :max_users, fill = :fill WHERE poolID = :poolID";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("source", pool.getSource());
        params.addValue("destination", pool.getDestination());
        params.addValue("date", pool.getDate());
        params.addValue("time", pool.getTime());
        params.addValue("creatorID", pool.getCreatorID().getRegistrationNumber()); // Foreign key reference to User
        params.addValue("max_users", pool.getmax_users());
        params.addValue("fill", pool.getFill());
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
}
