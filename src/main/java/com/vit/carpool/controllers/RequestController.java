package com.vit.carpool.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vit.carpool.entities.Request;
import com.vit.carpool.enums.RequestStatus;
import com.vit.carpool.services.RequestService;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    // Route to retrieve all requests
    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    // Route to find a request by id
    @GetMapping("/{requestId}")
    public ResponseEntity<Request> getRequestById(@PathVariable long requestId) {
        Request request = requestService.getRequestById(requestId);
        return ResponseEntity.ok(request);
    }

    // Route to create a new request
    @PostMapping
    public ResponseEntity<Integer> createRequest(@RequestBody Request request) {
        int result = requestService.createRequest(request);
        return ResponseEntity.ok(result);
    }

    // Route to update a request status
    @PutMapping("/{requestId}/status")
    public ResponseEntity<Integer> updateRequestStatus(@PathVariable long requestId,
            @RequestBody RequestStatus status) {
        int result = requestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok(result);
    }

    // Route to delete a request
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Integer> deleteRequest(@PathVariable long requestId) {
        int result = requestService.deleteRequest(requestId);
        return ResponseEntity.ok(result);
    }

    // Route to fetch all requests for pools created by a specific user
    @GetMapping("/by-creator/{creatorId}")
    public ResponseEntity<List<Request>> getRequestsByCreator(@PathVariable String creatorId) {
        List<Request> requests = requestService.getRequestsByCreator(creatorId);
        return ResponseEntity.ok(requests);
    }
}