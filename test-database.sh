#!/bin/bash

echo "Testing TSS Runtime Spring Boot Application"
echo "=========================================="

# Wait for application to start
echo "Waiting for application to start..."
sleep 5

# Test health endpoint
echo -e "\n1. Testing health endpoint..."
curl -s http://localhost:8080/api/users/health

# Test creating a user
echo -e "\n\n2. Creating a user..."
CREATE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890"
  }')

echo "Response: $CREATE_RESPONSE"

# Extract user ID from response
USER_ID=$(echo $CREATE_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
echo "Created user with ID: $USER_ID"

# Test getting all users
echo -e "\n3. Getting all users..."
curl -s http://localhost:8080/api/users | jq '.'

# Test getting user by ID
echo -e "\n4. Getting user by ID ($USER_ID)..."
curl -s http://localhost:8080/api/users/$USER_ID | jq '.'

# Test getting user by email
echo -e "\n5. Getting user by email..."
curl -s http://localhost:8080/api/users/email/john@example.com | jq '.'

# Test updating user
echo -e "\n6. Updating user..."
UPDATE_RESPONSE=$(curl -s -X PUT http://localhost:8080/api/users/$USER_ID \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "email": "john.updated@example.com",
    "phone": "0987654321"
  }')

echo "Update response: $UPDATE_RESPONSE"

# Test getting updated user
echo -e "\n7. Getting updated user..."
curl -s http://localhost:8080/api/users/$USER_ID | jq '.'

# Test creating another user
echo -e "\n8. Creating another user..."
curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane@example.com",
    "phone": "5555555555"
  }' | jq '.'

# Test getting all users again
echo -e "\n9. Getting all users (should show 2 users)..."
curl -s http://localhost:8080/api/users | jq '.'

# Test deleting user
echo -e "\n10. Deleting user with ID $USER_ID..."
curl -s -X DELETE http://localhost:8080/api/users/$USER_ID

# Test getting all users after deletion
echo -e "\n11. Getting all users after deletion (should show 1 user)..."
curl -s http://localhost:8080/api/users | jq '.'

echo -e "\n\nDatabase operations test completed!"
echo "Check the application logs to see the SQL queries being executed." 