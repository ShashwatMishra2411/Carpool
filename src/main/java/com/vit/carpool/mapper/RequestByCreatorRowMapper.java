package com.vit.carpool.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vit.carpool.entities.Pool;
import com.vit.carpool.entities.Request;
import com.vit.carpool.entities.RequestByCreator;
import com.vit.carpool.entities.User;
import com.vit.carpool.enums.RequestStatus;

/**
 * RowMapper implementation to map rows of a ResultSet to Request objects.
 */
public class RequestByCreatorRowMapper implements RowMapper<RequestByCreator> {

    /**
     * Maps a single row of the ResultSet to a Request object.
     *
     * @param rs     the ResultSet to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return the mapped Request object
     * @throws SQLException if a SQLException is encountered getting column values
     */
    @Override
    public RequestByCreator mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Create a new Request object
        RequestByCreator request = new RequestByCreator();
        request.setCreator_id(rs.getString("creator_id"));
        request.setUser_id(rs.getString("user_id"));
        request.setPool_id(rs.getLong("pool_id"));
        request.setRequest_id(rs.getLong("request_id"));
        request.setStatus(rs.getString("status"));
        // Return the populated Request object
        return request;
    }

}
