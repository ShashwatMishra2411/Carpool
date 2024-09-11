package com.vit.carpool.services;

import com.vit.carpool.entities.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class PoolService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to retrieve all pool entries
    public List<Pool> getAllPools() {
        String query = "SELECT * FROM pool";
        return jdbcTemplate.query(query, this::mapRowToPool);
    }

    // Method to find a pool by id
    public Pool getPoolById(long id) {
        String query = "SELECT * FROM pool WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, this::mapRowToPool);
    }

    // Method to create a new pool
    public int createPool(Pool pool) {
        String query = "INSERT INTO pool (id, name, block, place, date, time) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(query, pool.getID(), pool.getName(), pool.getBlock(), pool.getPlace(), pool.getDate(), pool.getTime());
    }

    // Method to update a pool
    public int updatePool(long id, Pool pool) {
        String query = "UPDATE pool SET name = ?, block = ?, place = ?, date = ?, time = ? WHERE id = ?";
        return jdbcTemplate.update(query, pool.getName(), pool.getBlock(), pool.getPlace(), pool.getDate(), pool.getTime(), id);
    }

    // Method to delete a pool
    public int deletePool(long id) {
        String query = "DELETE FROM pool WHERE id = ?";
        return jdbcTemplate.update(query, id);
    }

    // Method to map a result set row to a Pool object
    private Pool mapRowToPool(ResultSet rs, int rowNum) throws SQLException {
        Pool pool = new Pool();
        pool.setName(rs.getString("name"));
        pool.setBlock(rs.getString("block"));
        pool.setPlace(rs.getString("place"));
        pool.setDate(rs.getDate("date").toLocalDate());
        pool.setTime(rs.getTime("time").toLocalTime());
        pool.setID(rs.getLong("id"));
//        System.out.println("rs = "+rs);
        return pool;
    }
}
