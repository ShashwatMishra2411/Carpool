package com.vit.carpool.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vit.carpool.entities.Pool;
import com.vit.carpool.entities.Request;
import com.vit.carpool.entities.User;
import com.vit.carpool.enums.RequestStatus;

public class RequestRowMapper implements RowMapper<Request> {

    @Override
    public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
        Request request = new Request();
        request.setStatus(RequestStatus.valueOf(rs.getString("status")));

        Pool pool = new Pool();
        pool.setPoolID(rs.getLong("pool_id"));
        pool.setSource(rs.getString("source"));
        pool.setDestination(rs.getString("destination"));
        pool.setDate(rs.getDate("date").toLocalDate());
        pool.setTime(rs.getTime("time").toLocalTime());
        request.setPool(pool);

        User requester = new User();
        requester.setRegistrationNumber(rs.getString("requester"));
        requester.setName(rs.getString("requester_name"));
        request.setUser(requester);

        return request;
    }
}