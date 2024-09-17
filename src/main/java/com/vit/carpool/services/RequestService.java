package com.vit.carpool.services;

import java.util.List;
import java.util.Map;

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

        // * UPDATE THE REQUEST STATUS
        String updateStatusQuery = "UPDATE request SET status = :status WHERE request_id = :requestId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status.name());
        params.addValue("requestId", requestId);
        int rowsAffected = namedParameterJdbcTemplate.update(updateStatusQuery, params);

        if (status == RequestStatus.APPROVED) {
            // ? REMOVE THE USER_ID FROM ALL ROWS IN THE REQUEST TABLE AS USER GOT ACCEPTED
            // BY THE CREATOR

            // * retrieve the user_id and pool_id associated with the given requestId

            String getUserQuery = "SELECT user_id, pool_id FROM request where request_id = :requestId";

            Map<String, Object> result = namedParameterJdbcTemplate.queryForMap(getUserQuery, params);
            String userId = (String) result.get("user_id");
            Long poolId = (Long) result.get("pool_id");

            // * ADD THE USER_ID TO THE USERS FIELD IN THE POOL TABLE

            String addUserToPoolQuery = "UPDATE pool SET users = array_append(users, :userId) WHERE poolID = :poolId";
            MapSqlParameterSource addUserToPoolParams = new MapSqlParameterSource();

            addUserToPoolParams.addValue("userId", userId);
            addUserToPoolParams.addValue("poolId", poolId);
            namedParameterJdbcTemplate.update(addUserToPoolQuery, addUserToPoolParams);

            // * REMOVE THE USER_ID FROM ALL ROWS IN THE REQUEST TABLE

            String removeUserFromRequestQuery = "DELETE from request where user_id = :userId";

            MapSqlParameterSource removeUserParams = new MapSqlParameterSource();
            removeUserParams.addValue("userId", userId);
            namedParameterJdbcTemplate.update(removeUserFromRequestQuery, removeUserParams);
        }

        return rowsAffected;
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