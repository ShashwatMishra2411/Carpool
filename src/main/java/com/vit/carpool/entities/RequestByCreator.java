package com.vit.carpool.entities;

public class RequestByCreator {
    private long request_id;
    private String status;
    private String creator_id;
    private String user_id;
    private long pool_id;

    // Getter and Setter for request_id
    public long getRequest_id() {
        return request_id;
    }

    public void setRequest_id(long request_id) {
        this.request_id = request_id;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter and Setter for creator_id
    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    // Getter and Setter for user_id
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    // Getter and Setter for pool_id
    public long getPool_id() {
        return pool_id;
    }

    public void setPool_id(long pool_id) {
        this.pool_id = pool_id;
    }
}
