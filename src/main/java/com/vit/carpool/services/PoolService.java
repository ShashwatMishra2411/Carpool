package com.vit.carpool.services;

import com.vit.carpool.entities.Combined;
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
    public List<Combined> getAllPools() {
        String query = "SELECT p.*, u.registrationnumber AS creator_registrationnumber, u.name AS creator_name FROM pool p LEFT JOIN users u ON p.creatorID = u.registrationnumber";
        return namedParameterJdbcTemplate.query(query, new BeanPropertyRowMapper<>(Combined.class));
    }

    // Method to find a pool by id
    public Pool getPoolById(long poolID) {
        String query = "SELECT p.*, u.registrationnumber AS creator_registrationnumber, u.name AS creator_name " +
                "FROM pool p " +
                "LEFT JOIN users u ON p.creatorID = u.registrationnumber " +
                "WHERE p.poolID = :poolID";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("poolID", poolID);
        return namedParameterJdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(Pool.class));
    }

    // Method to create a new pool
    @Transactional
    public int createPool(Pool pool) {
        String query = "INSERT INTO pool (source, destination, date, time, creatorID, max_users, fill) " +
                "VALUES (:source, :destination, :date, :time, :creatorID, :max_users, :fill)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("source", pool.getSource());
        params.addValue("destination", pool.getDestination());
        params.addValue("date", pool.getDate());
        params.addValue("time", pool.getTime());
        params.addValue("creatorID", pool.getCreator().getRegistrationNumber()); // Foreign key reference to User
        params.addValue("max_users", pool.getMaxUsers());
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
        params.addValue("creatorID", pool.getCreator().getRegistrationNumber()); // Foreign key reference to User
        params.addValue("max_users", pool.getMaxUsers());
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