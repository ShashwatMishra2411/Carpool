package com.vit.carpool.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pool {
    private String name;
    private String block;
    private String place;
    private LocalDate date;
    private LocalTime time;
    private long id;


    public Pool() {}


    public Pool(String name, String block, String place, LocalDate date, LocalTime time) {
        this.name = name;
        this.block = block;
        this.place = place;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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
    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }
}
