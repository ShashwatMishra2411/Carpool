package com.vit.carpool.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vit.carpool.entities.Pool;
import com.vit.carpool.entities.Request;
import com.vit.carpool.entities.User;
import com.vit.carpool.enums.RequestStatus;

/**
 * RowMapper implementation to map rows of a ResultSet to Request objects.
 */
public class RequestRowMapper implements RowMapper<Request> {

    /**
     * Maps a single row of the ResultSet to a Request object.
     *
     * @param rs     the ResultSet to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return the mapped Request object
     * @throws SQLException if a SQLException is encountered getting column values
     */
    @Override
    public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Create a new Request object
        Request request = new Request();

        // Create User objects for requester and creator
        User requester = new User();
        User creator = new User();

        // Set the status of the request
        request.setStatus(RequestStatus.valueOf(rs.getString("status")));

        // Set the request ID
        request.setRequestId(rs.getLong("request_id"));

        // Create and populate a Pool object
        Pool pool = new Pool();
        pool.setPoolID(rs.getLong("pool_id"));
        pool.setSource(rs.getString("source"));
        pool.setDestination(rs.getString("destination"));
        pool.setDate(rs.getDate("date").toLocalDate());
        pool.setTime(rs.getTime("time").toLocalTime());

        // Set the creator's registration number and name in the Pool object
        creator.setRegistrationNumber(rs.getString("creator"));
        creator.setName(rs.getString("creator_name"));
        pool.setCreator(creator);

        // Set the requester's registration number and name
        requester.setRegistrationNumber(rs.getString("requester"));
        requester.setName(rs.getString("requester_name"));

        // Set the requester and creator in the Request object
        request.setUser(requester);
        request.setCreator(creator);

        // Set the Pool object in the Request object
        request.setPool(pool);

        // Return the populated Request object
        return request;
    }
}