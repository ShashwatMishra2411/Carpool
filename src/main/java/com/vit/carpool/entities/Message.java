package com.vit.carpool.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "messageid", nullable = false, updatable = false)
    private UUID messageId;

    @Column(name = "senderid", nullable = false, length = 255)
    private String senderId;

    @Column(name = "groupid", nullable = false)
    private Long groupId;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "timestamp", columnDefinition = "timestamp with time zone", nullable = false, updatable = false)
    private String timestamp;

    // Getters and Setters

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

