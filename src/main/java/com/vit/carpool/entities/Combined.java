package com.vit.carpool.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
// @Table(name = "new_table_name") // Replace with your actual table name
public class Combined {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poolID; // Primary key

    private String source; // Source of the pool
    private String destination; // Destination of the pool

    private LocalDate date; // Date of the pool
    private LocalTime time; // Time of the pool

    private int max_users; // Maximum number of users allowed
    private int fill; // Current number of users in the pool

    @Column(name = "creatorid")
    private String creatorID; // Creator ID (can store registration number)

    @Column(name = "users", columnDefinition = "text[]")
    private String[] users; // Array of user registration numbers

    @Transient // Creator's name is not stored in the actual table but retrieved via a join
    private String creatorName; // Creator's name

    // Getters and setters
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

    public int getMaxUsers() {
        return max_users;
    }

    public void setMaxUsers(int max_users) {
        this.max_users = max_users;
    }

    public int getFill() {
        return fill;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}