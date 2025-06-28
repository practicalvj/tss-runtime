# TSS Runtime - Spring Boot PostgreSQL Application

A simple Spring Boot application that demonstrates REST API operations with PostgreSQL database integration.

## Features

- RESTful API endpoints for CRUD operations on User entities
- PostgreSQL database integration with JPA/Hibernate
- Comprehensive test coverage
- Input validation
- Error handling

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

## Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE tss_runtime;
```

2. Create a user (optional, you can use the default postgres user):
```sql
CREATE USER tss_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE tss_runtime TO tss_user;
```

3. Update the database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tss_runtime
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

### Using Maven
```bash
mvn spring-boot:run
```

### Using JAR file
```bash
mvn clean package
java -jar target/tss-runtime-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### Health Check
- **GET** `/api/users/health` - Check if the service is running

### User Operations
- **GET** `/api/users` - Get all users
- **GET** `/api/users/{id}` - Get user by ID
- **GET** `/api/users/email/{email}` - Get user by email
- **POST** `/api/users` - Create a new user
- **PUT** `/api/users/{id}` - Update an existing user
- **DELETE** `/api/users/{id}` - Delete a user

### Example Requests

#### Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890"
  }'
```

#### Get All Users
```bash
curl -X GET http://localhost:8080/api/users
```

#### Get User by ID
```bash
curl -X GET http://localhost:8080/api/users/1
```

#### Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "email": "john.updated@example.com",
    "phone": "1234567890"
  }'
```

#### Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=UserControllerTest
```

### Run with coverage
```bash
mvn test jacoco:report
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/tssruntime/
│   │   ├── TssRuntimeApplication.java
│   │   ├── controller/
│   │   │   └── UserController.java
│   │   ├── model/
│   │   │   └── User.java
│   │   ├── repository/
│   │   │   └── UserRepository.java
│   │   └── service/
│   │       └── UserService.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/example/tssruntime/
    │   ├── TssRuntimeApplicationTests.java
    │   ├── controller/
    │   │   └── UserControllerTest.java
    │   └── service/
    │       └── UserServiceTest.java
    └── resources/
        └── application-test.properties
```

## Database Schema

The application automatically creates the following table:

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255)
);
```

## Configuration

Key configuration properties in `application.properties`:

- `spring.jpa.hibernate.ddl-auto=create-drop` - Creates tables on startup and drops them on shutdown
- `spring.jpa.show-sql=true` - Shows SQL queries in logs
- `server.port=8080` - Application port

## Testing Database Operations

The application includes comprehensive tests that verify:

1. **User Creation**: Records are properly inserted into the database
2. **User Retrieval**: Records can be fetched by ID and email
3. **User Updates**: Records are properly updated in the database
4. **User Deletion**: Records are properly removed from the database
5. **Validation**: Invalid data is properly rejected
6. **Error Handling**: Proper error responses for missing records

To verify database operations are working:

1. Start the application
2. Create a user using the POST endpoint
3. Retrieve the user using the GET endpoint
4. Check the application logs to see the SQL queries
5. Run the tests to verify all operations work correctly

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Verify database credentials in `application.properties`
- Check if the database exists

### Port Already in Use
- Change the port in `application.properties`: `server.port=8081`

### Test Failures
- Ensure you have Java 17+ installed
- Run `mvn clean test` to clear any cached test data 