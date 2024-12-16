package com.vit.carpool.controllers;

import com.vit.carpool.entities.Combined;
import com.vit.carpool.entities.Pool;
import com.vit.carpool.services.PoolService;

import java.util.ArrayList;
import java.util.List;
import com.vit.carpool.entities.RemoveUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import java.util.List;
// public class RemoveUser {
//     private String userid;
//     private long poolid;

//     public String getUserid() {
//         return userid;
//     }

//     public void setUserid(String userid) {
//         this.userid = userid;
//     }

//     public long getPoolid() {
//         return poolid;
//     }

//     public void setPoolid(long poolid) {
//         this.poolid = poolid;
//     }
// }

@RestController

@RequestMapping("/pools")
public class PoolController {
    @Autowired
    private PoolService poolService;

    @GetMapping
    public ResponseEntity<?> getAllPools() {
        try {
            return new ResponseEntity<>(poolService.getAllPools(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving pools: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPoolById(@PathVariable long id) {
        try {
            Combined pool = poolService.getPoolById(id);
            return new ResponseEntity<>(pool, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Pool not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving pool: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pool/{id}")
    public ResponseEntity<?> getPoolByUserId(@PathVariable long id) {
        List<Combined> pools = poolService.getPoolByUserId(id);
        return new ResponseEntity<>(pools, HttpStatus.OK);
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping
    public ResponseEntity<?> createPool(@RequestBody Pool pool) {
        try {
            poolService.createPool(pool);
            return new ResponseEntity<>("Pool created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePool(@PathVariable long id, @RequestBody Pool pool) {
        try {
            int rowsAffected = poolService.updatePool(id, pool);
            if (rowsAffected > 0) {
                return new ResponseEntity<>("Pool updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Pool not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating pool: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePool(@PathVariable long id) {
        try {
            int rowsAffected = poolService.deletePool(id);
            if (rowsAffected > 0) {
                return new ResponseEntity<>("Pool deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Pool not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting pool: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove-user")
    public ResponseEntity<?> removeUser(@RequestBody RemoveUser object) {
        try {

            int rowsAffected = poolService.removeUserFromPool(object.getPoolid(), object.getUserid());
            if (rowsAffected > 0) {
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}