package com.vit.carpool.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vit.carpool.entities.Request;
import com.vit.carpool.entities.RequestByCreator;
import com.vit.carpool.enums.RequestStatus;
import com.vit.carpool.mapper.RequestByCreatorRowMapper;
import com.vit.carpool.mapper.RequestRowMapper;

@Service
public class RequestService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Method to retrieve all requests
    public List<Request> getAllRequests() {
        String query = """
                SELECT
                    r.request_id,
                	p.poolid as pool_id,
                    p.source,
                    p.destination,
                    p.date,
                    p.time,
                    p.creatorid,
                    r.status,
                    u.registrationnumber AS requester,
                    u.name AS requester_name,
                    u1.registrationnumber AS creator,
                    u1.name AS creator_name
                FROM
                    request r
                JOIN
                    pool p ON r.pool_id = p.poolID
                JOIN
                    users u ON r.user_id = u.registrationnumber
                JOIN
                    users u1 ON r.creator_id = u1.registrationnumber;
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(query, params, new RequestRowMapper());
    }

    // Method to find a request by id
    public Request getRequestById(long requestId) {
        String query = """
                SELECT
                    r.request_id,
                	p.poolid as pool_id,
                    p.source,
                    p.destination,
                    p.date,
                    p.time,
                    p.creatorid,
                    r.status,
                    u.registrationnumber AS requester,
                    u.name AS requester_name,
                    u1.registrationnumber AS creator,
                    u1.name AS creator_name
                FROM
                    request r
                JOIN
                    pool p ON r.pool_id = p.poolID
                JOIN
                    users u ON r.user_id = u.registrationnumber
                JOIN
                    users u1 ON r.creator_id = u1.registrationnumber
                WHERE
                    r.request_id = :requestId;
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("requestId", requestId);
        return namedParameterJdbcTemplate.queryForObject(query, params, new RequestRowMapper());
    }

    // Method to create a new request
    @Transactional
    public int createRequest(Request request) throws SQLException {
        String checkQuery = "SELECT poolid FROM pool WHERE :userId = ANY(users);";
        MapSqlParameterSource checkParams = new MapSqlParameterSource();
        checkParams.addValue("userId", request.getUser().getRegistrationNumber());
        List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(checkQuery, checkParams);

        if (result.size() > 0) {
            System.out.println(result);
            throw new IllegalArgumentException("User already in a pool");
        } else {
            try {
                String query = "INSERT INTO request (pool_id, creator_id, user_id, status) " +
                        "VALUES (:pool_id, :creator_id, :user_id, :status)";
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("pool_id", request.getPool().getPoolID());
                params.addValue("creator_id", request.getCreator().getRegistrationNumber());
                params.addValue("user_id", request.getUser().getRegistrationNumber());
                params.addValue("status", request.getStatus().name());
                return namedParameterJdbcTemplate.update(query, params);
            } catch (DataAccessException e) {
                throw new SQLException("Failed to create request: " + e.getMessage(), e);
            }

        }

    }

    // Method to update a request status
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

            if (result != null) {
                String userId = (String) result.get("user_id");
                Long poolId = (Long) result.get("pool_id");

                // * ADD THE USER_ID TO THE USERS FIELD IN THE POOL TABLE
                MapSqlParameterSource poolParam = new MapSqlParameterSource();
                poolParam.addValue("poolid", poolId);
                List<Map<String, Object>> pool = namedParameterJdbcTemplate
                        .queryForList("SELECT * FROM pool WHERE poolid = :poolid", poolParam);

                if (!pool.isEmpty()) {
                    int filled = Integer.parseInt(pool.get(0).get("fill").toString());
                    int max = Integer.parseInt(pool.get(0).get("max_users").toString()); // Use correct column

                    if (max <= filled) {
                        // The condition where the number of users in the pool exceeds the max capacity
                        deleteRequest(requestId); // Delete request before throwing exception
                        throw new IllegalArgumentException(
                                "The number of filled users cannot exceed the maximum allowed.");
                    }

                    // Add the user to the pool if the condition is met
                    String addUserToPoolQuery = "UPDATE pool SET users = array_append(users, :userId), fill = fill + 1 WHERE poolID = :poolId";
                    MapSqlParameterSource addUserToPoolParams = new MapSqlParameterSource();
                    addUserToPoolParams.addValue("userId", userId);
                    addUserToPoolParams.addValue("poolId", poolId);
                    namedParameterJdbcTemplate.update(addUserToPoolQuery, addUserToPoolParams);

                    // * REMOVE THE USER_ID FROM ALL ROWS IN THE REQUEST TABLE
                    String removeUserFromRequestQuery = "DELETE FROM request WHERE user_id = :userId";
                    MapSqlParameterSource removeUserParams = new MapSqlParameterSource();
                    removeUserParams.addValue("userId", userId);
                    namedParameterJdbcTemplate.update(removeUserFromRequestQuery, removeUserParams);
                }
            }
        }

        return rowsAffected;
    }

    // Method to delete a request
    @Transactional
    public int deleteRequest(long requestId) {
        String query = "DELETE FROM request WHERE request_id = :requestId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("requestId", requestId);
        return namedParameterJdbcTemplate.update(query, params);
    }

    // Method to fetch all requests for pools created by a specific user
    public List<RequestByCreator> getRequestsByCreator(String creatorId) {
        System.out.println(creatorId);
        String query = "SELECT r.request_id,r.status,r.pool_id,r.creator_id,r.user_id FROM request r " +
                "JOIN pool p ON r.pool_id = p.poolid " +
                "WHERE p.creatorid = :creatorId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("creatorId", creatorId);
        return namedParameterJdbcTemplate.query(query, params, new RequestByCreatorRowMapper());
    }
}