package com.vit.carpool.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Array;
import org.springframework.jdbc.core.RowMapper;
import com.vit.carpool.entities.Combined;

public class CombinedRowMapper implements RowMapper<Combined> {
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
