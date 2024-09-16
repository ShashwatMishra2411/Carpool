package com.vit.carpool.entities;

import com.vit.carpool.enums.RequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The Request entity represents a request made by a user to join a pool.
 * Each request is uniquely identified by a requestId.
 */
@Entity
@Table(name = "request")
public class Request {

    /**
     * The unique identifier for each request.
     * This value is auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    /**
     * The pool to which the request is made.
     * This is a many-to-one relationship with the Pool entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pool_id", referencedColumnName = "poolID")
    private Pool pool;

    /**
     * The user who created the pool.
     * This is a many-to-one relationship with the User entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "registrationnumber")
    private User creator;

    /**
     * The user who is making the request to join the pool.
     * This is a many-to-one relationship with the User entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "registrationnumber")
    private User user;

    /**
     * The status of the request.
     * This is an enumerated value stored as a string in the database.
     * Possible values are defined in the RequestStatus enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    // Getters and Setters

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}