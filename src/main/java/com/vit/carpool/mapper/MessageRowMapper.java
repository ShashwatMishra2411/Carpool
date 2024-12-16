package com.vit.carpool.mapper;

import com.vit.carpool.entities.Message;
import org.springframework.jdbc.core.RowMapper;
import java.util.UUID;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setMessageId((UUID) rs.getObject("messageid"));
        message.setSenderId(rs.getString("senderid"));
        message.setGroupId(rs.getLong("groupid"));
        message.setContent(rs.getString("content"));
        message.setTimestamp(rs.getString("timestamp"));
        return message;
    }
}
