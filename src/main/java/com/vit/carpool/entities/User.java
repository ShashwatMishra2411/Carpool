package com.vit.carpool.entities;

// import org.hibernate.mapping.List;
import java.util.ArrayList;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "registrationnumber", nullable = false)
    private String registrationNumber;

    @Column(name = "name", nullable = false)
    private String name;

    private ArrayList<Long> createdPools = new ArrayList<>(); // Multiple pools created by the user

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "gender", nullable = false)
    String gender;

    long joinedpool;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Long> getCreatedPools() {

        return createdPools;
    }

    public void setCreatedPools(ArrayList<Long> createdPools) {
        this.createdPools = createdPools;
    }

    public long getJoined() {
        return joinedpool;
    }

    public void setJoined(Long id) {
        this.joinedpool = id;
    }
}
