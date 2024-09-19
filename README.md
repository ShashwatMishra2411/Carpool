# Table of Contents

- [Pool Section](#pool-section)
- [Request Section](#request-section)

## Pool Section

### **1. GET /pools**

#### Description:

Retrieve a list of all available pools.

#### cURL Command:

```bash
curl -X GET http://localhost:8080/pools
```

#### Expected Response (200 OK):

```json
[
  {
    "poolID": 1,
    "source": "Location A",
    "destination": "Location B",
    "date": "2024-09-19",
    "time": "08:00",
    "creatorID": "user123",
    "maxUsers": 4,
    "fill": 2,
    "users": ["user1", "user2"],
    "creatorName": "John Doe"
  }
]
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error retrieving pools: <error message>"
}
```

---

### **2. GET /pools/{id}**

#### Description:

Retrieve details of a specific pool by its ID.

#### cURL Command:

```bash
curl -X GET http://localhost:8080/pools/1
```

#### Expected Response (200 OK):

```json
{
  "poolID": 1,
  "source": "Location A",
  "destination": "Location B",
  "date": "2024-09-19",
  "time": "08:00",
  "creatorID": "user123",
  "maxUsers": 4,
  "fill": 2,
  "users": ["user1", "user2"],
  "creatorName": "John Doe"
}
```

#### Error Response (404 Not Found):

```json
{
  "error": "Pool not found"
}
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error retrieving pool: <error message>"
}
```

---

### **3. POST /pools**

#### Description:

Create a new pool.

#### cURL Command:

```bash
curl -X POST http://localhost:8080/pools \
-H "Content-Type: application/json" \
-d '{
    "source": "Location A",
    "destination": "Location B",
    "date": "2024-09-19",
    "time": "08:00",
    "creatorID": "user123",
    "maxUsers": 4,
    "fill": 2,
    "users": ["user1", "user2"]
}'
```

#### Request Body:

```json
{
  "source": "Location A",
  "destination": "Location B",
  "date": "2024-09-19",
  "time": "08:00",
  "creatorID": "user123",
  "maxUsers": 4,
  "fill": 2,
  "users": ["user1", "user2"]
}
```

#### Expected Response (201 Created):

```json
{
  "message": "Pool created successfully"
}
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error creating pool: <error message>"
}
```

---

### **4. PUT /pools/{id}**

#### Description:

Update an existing pool by its ID.

#### cURL Command:

```bash
curl -X PUT http://localhost:8080/pools/1 \
-H "Content-Type: application/json" \
-d '{
    "source": "Location A",
    "destination": "Location B",
    "date": "2024-09-20",
    "time": "09:00",
    "creatorID": "user123",
    "maxUsers": 5,
    "fill": 3,
    "users": ["user1", "user2", "user3"]
}'
```

#### Request Body:

```json
{
  "source": "Location A",
  "destination": "Location B",
  "date": "2024-09-20",
  "time": "09:00",
  "creatorID": "user123",
  "maxUsers": 5,
  "fill": 3,
  "users": ["user1", "user2", "user3"]
}
```

#### Expected Response (200 OK):

```json
{
  "message": "Pool updated successfully"
}
```

#### Error Response (404 Not Found):

```json
{
  "error": "Pool not found"
}
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error updating pool: <error message>"
}
```

---

### **5. DELETE /pools/{id}**

#### Description:

Delete an existing pool by its ID.

#### cURL Command:

```bash
curl -X DELETE http://localhost:8080/pools/1
```

#### Expected Response (200 OK):

```json
{
  "message": "Pool deleted successfully"
}
```

#### Error Response (404 Not Found):

```json
{
  "error": "Pool not found"
}
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error deleting pool: <error message>"
}
```

## Request Section

### **1. GET /api/requests**

#### Description:

Retrieve all requests.

#### cURL Command:

```bash
curl -X GET http://localhost:8080/api/requests
```

#### Expected Response (200 OK):

```json
[
  {
    "poolId": 1,
    "source": "Location A",
    "destination": "Location B",
    "date": "2024-09-19",
    "time": "08:00",
    "status": "PENDING",
    "requester": "user1",
    "requesterName": "John Doe"
  }
]
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error retrieving requests: <error message>"
}
```

---

### **2. GET /api/requests/{requestId}**

#### Description:

Retrieve details of a specific request by its ID.

#### cURL Command:

```bash
curl -X GET http://localhost:8080/api/requests/1
```

#### Expected Response (200 OK):

```json
{
  "poolId": 1,
  "source": "Location A",
  "destination": "Location B",
  "date": "2024-09-19",
  "time": "08:00",
  "status": "PENDING",
  "requester": "user1",
  "requesterName": "John Doe",
  "creatorId": "user123",
  "creatorName": "Jane Doe"
}
```

#### Error Response (404 Not Found):

```json
{
  "error": "Request not found"
}
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error retrieving request: <error message>"
}
```

---

### **3. POST /api/requests**

#### Description:

Create a new request.

#### cURL Command:

```bash
curl -X POST http://localhost:8080/api/requests \
-H "Content-Type: application/json" \
-d '{
    "pool": {
        "poolID": 1
    },
    "creator": {
        "registrationNumber": "user123"
    },
    "user": {
        "registrationNumber": "user1"
    },
    "status": "PENDING"
}'
```

#### Request Body:

```json
{
  "pool": {
    "poolID": 1
  },
  "creator": {
    "registrationNumber": "user123"
  },
  "user": {
    "registrationNumber": "user1"
  },
  "status": "PENDING"
}
```

#### Expected Response (200 OK):

```json
{
  "message": "Request created successfully"
}
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error creating request: <error message>"
}
```

---

### **4. PUT /api/requests/{requestId}/status**

#### Description:

Update the status of a specific request by its ID.

#### cURL Command:

```bash
curl -X PUT http://localhost:8080/api/requests/1/status \
-H "Content-Type: application/json" \
-d '{
    "status": "APPROVED"
}'
```

#### Request Body:

```json
{
  "status": "APPROVED"
}
```

#### Expected Response (200 OK):

```json
{
  "message": "Request status updated successfully"
}
```

#### Error Response (400 Bad Request):

```json
{
  "error": "Invalid request body"
}
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error updating request status: <error message>"
}
```

---

### **5. DELETE /api/requests/{requestId}**

#### Description:

Delete a specific request by its ID.

#### cURL Command:

```bash
curl -X DELETE http://localhost:8080/api/requests/1
```

#### Expected Response (200 OK):

```json
{
  "message": "Request deleted successfully"
}
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error deleting request: <error message>"
}
```

---

### **6. GET /api/requests/by-creator/{creatorId}**

#### Description:

Retrieve all requests for pools created by a specific user.

#### cURL Command:

```bash
curl -X GET http://localhost:8080/api/requests/by-creator/user123
```

#### Expected Response (200 OK):

```json
[
  {
    "poolId": 1,
    "source": "Location A",
    "destination": "Location B",
    "date": "2024-09-19",
    "time": "08:00",
    "status": "PENDING",
    "requester": "user1",
    "requesterName": "John Doe"
  }
]
```

#### Error Response (500 Internal Server Error):

```json
{
  "error": "Error retrieving requests by creator: <error message>"
}
```
