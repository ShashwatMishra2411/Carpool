package com.vit.carpool.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "registrationnumber", nullable = false)
    private String registrationNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pool> createdPools; // List of pools created by the user

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pool> getCreatedPools() {
        return createdPools;
    }

    public void setCreatedPools(List<Pool> createdPools) {
        this.createdPools = createdPools;
    }
}
