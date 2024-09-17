package com.vit.carpool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vit.carpool.entities.Request;
import com.vit.carpool.enums.RequestStatus;
import com.vit.carpool.mapper.RequestByIdRowMapper;
import com.vit.carpool.mapper.RequestRowMapper;

@Service
public class RequestService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Method to retrieve all requests
    public List<Request> getAllRequests() {
        String query = """
                SELECT
                	p.poolid as pool_id,
                    p.source,
                    p.destination,
                    p.date,
                    p.time,
                    r.status,
                    u.registrationnumber AS requester,
                    u.name AS requester_name
                FROM
                    request r
                JOIN
                    pool p ON r.pool_id = p.poolID
                JOIN
                    users u ON r.user_id = u.registrationnumber;
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(query, params, new RequestRowMapper());
    }

    // Method to find a request by id
    public Request getRequestById(long requestId) {
        String query = """
                SELECT
                	p.poolid as pool_id,
                    p.source,
                    p.destination,
                    p.date,
                    p.time,
                    r.status,
                    u.registrationnumber AS requester,
                    u.name AS requester_name,
                	c.registrationnumber AS creator_id,
                	c.name AS creator_name
                FROM
                    request r
                JOIN
                    pool p ON r.pool_id = p.poolID
                JOIN
                    users u ON r.user_id = u.registrationnumber
                JOIN
                	users c ON r.creator_id = c.registrationnumber
                WHERE
                    r.request_id = :requestId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("requestId", requestId);
        return namedParameterJdbcTemplate.queryForObject(query, params, new RequestByIdRowMapper());
    }

    // Method to create a new request
    @Transactional
    public int createRequest(Request request) {
        String query = "INSERT INTO request (pool_id, creator_id, user_id, status) " +
                "VALUES (:pool_id, :creator_id, :user_id, :status)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("pool_id", request.getPool().getPoolID());
        params.addValue("creator_id", request.getCreator().getRegistrationNumber());
        params.addValue("user_id", request.getUser().getRegistrationNumber());
        params.addValue("status", request.getStatus().name());
        return namedParameterJdbcTemplate.update(query, params);
    }

    // Method to update a request status
    @Transactional
    public int updateRequestStatus(long requestId, RequestStatus status) {
        String query = "UPDATE request SET status = :status WHERE requestId = :requestId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status.name());
        params.addValue("requestId", requestId);
        return namedParameterJdbcTemplate.update(query, params);
    }

    // Method to delete a request
    @Transactional
    public int deleteRequest(long requestId) {
        String query = "DELETE FROM request WHERE requestId = :requestId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("requestId", requestId);
        return namedParameterJdbcTemplate.update(query, params);
    }

    // Method to fetch all requests for pools created by a specific user
    public List<Request> getRequestsByCreator(String creatorId) {
        String query = "SELECT r.* FROM request r " +
                "JOIN pool p ON r.pool_id = p.poolID " +
                "WHERE p.creator_id = :creatorId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("creatorId", creatorId);
        return namedParameterJdbcTemplate.query(query, params, new BeanPropertyRowMapper<>(Request.class));
    }
}