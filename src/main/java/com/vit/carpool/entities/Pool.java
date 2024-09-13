package com.vit.carpool.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Pool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poolID; // Primary Key

    private String source; // Source of the pool
    private String destination; // Destination of the pool
    private LocalDate date; // Date of the pool
    private LocalTime time; // Time of the pool

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users; // List of users participating in the pool

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorID", referencedColumnName = "registrationNumber")
    private User creatorID; // Creator of the pool (foreign key)

    private int max_users; // Maximum number of users in the pool
    private int fill; // Current number of users in the pool

    // Constructors, Getters and Setters
    public Pool() {
    }

    public Pool(String source, String destination, LocalDate date, LocalTime time,
            User creatorID, int max_users, int fill) {
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.creatorID = creatorID;
        this.max_users = max_users;
        this.fill = fill;
    }

    // Getters and setters for each field
    public Long getPoolID() {
        return poolID;
    }

    public void setPoolID(Long poolID) {
        this.poolID = poolID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(User creatorID) {
        this.creatorID = creatorID;
    }

    public int getmax_users() {
        return max_users;
    }

    public void setmax_users(int max_users) {
        this.max_users = max_users;
    }

    public int getFill() {
        return fill;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }
}
