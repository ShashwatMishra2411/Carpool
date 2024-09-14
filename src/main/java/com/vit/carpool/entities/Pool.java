package com.vit.carpool.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
// import java.util.List;

@Entity
@Table(name = "pool")
public class Pool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poolID;

    private String source;
    private String destination;
    private LocalDate date;
    private LocalTime time;
    private int maxUsers;
    private int fill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorID")
    private User creator;

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
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getFill() {
        return fill;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}