package com.vit.carpool.services;

import com.vit.carpool.entities.Message;
import com.vit.carpool.mapper.MessageRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Message> getAllMessages() {
        String query = "SELECT * FROM messages";
        return namedParameterJdbcTemplate.query(query, new MessageRowMapper());
    }

    public List<Message> getMessageById(long messageId) {
        try {
            String query = "SELECT * FROM messages WHERE groupid = :messageid";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("messageid", messageId);
            return namedParameterJdbcTemplate.query(query, params, new MessageRowMapper());
        } catch (Exception e) {
            throw new RuntimeException("Message with ID " + messageId + " not found: " + e.getMessage());
        }
    }

    @Transactional
    public int createMessage(Message message) {
        String query = "INSERT INTO messages (messageid, senderid, groupid, content, timestamp) " +
                "VALUES (:messageid, :senderid, :groupid, :content, :timestamp)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("messageid", UUID.randomUUID())
                .addValue("senderid", message.getSenderId())
                .addValue("groupid", message.getGroupId())
                .addValue("content", message.getContent())
                .addValue("timestamp", message.getTimestamp());

        return namedParameterJdbcTemplate.update(query, params);
    }

    @Transactional
    public int updateMessage(UUID messageId, Message messageDetails) {
        String query = "UPDATE messages SET senderid = :senderid, groupid = :groupid, content = :content, timestamp = :timestamp " +
                "WHERE messageid = :messageid";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("messageid", messageId)
                .addValue("senderid", messageDetails.getSenderId())
                .addValue("groupid", messageDetails.getGroupId())
                .addValue("content", messageDetails.getContent())
                .addValue("timestamp", messageDetails.getTimestamp());

        return namedParameterJdbcTemplate.update(query, params);
    }

    @Transactional
    public int deleteMessage(UUID messageId) {
        String query = "DELETE FROM messages WHERE messageid = :messageid";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("messageid", messageId);
        return namedParameterJdbcTemplate.update(query, params);
    }
}
