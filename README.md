# TSS Runtime - Spring Boot PostgreSQL Application

A Spring Boot application demonstrating Taskana integration with PostgreSQL.

## Features

- RESTful API endpoints for Taskana workbaskets, classifications, and tasks
- PostgreSQL database integration
- Input validation and error handling

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

The application will start on `http://localhost:8081`

## API Endpoints

### Workbasket Operations
- **GET** `/api/taskana/workbaskets` - List all workbaskets
- **POST** `/api/taskana/workbaskets` - Create a new workbasket

#### Example: Create a Workbasket
```bash
curl -X POST http://localhost:8081/api/taskana/workbaskets \
  -H "Content-Type: application/json" \
  -d '{
    "key": "WORKBASKET1",
    "name": "My Workbasket",
    "description": "A test workbasket",
    "domain": "DOMAIN_A"
  }'
```

### Classification Operations
- **GET** `/api/taskana/classifications` - List all classifications
- **POST** `/api/taskana/classifications` - Create a new classification

#### Example: Create a Classification
```bash
curl -X POST http://localhost:8081/api/taskana/classifications \
  -H "Content-Type: application/json" \
  -d '{
    "key": "TASK",
    "name": "Task Classification",
    "description": "Default task classification",
    "domain": "DOMAIN_A",
    "category": "MANUAL"
  }'
```

### Task Operations
- **GET** `/api/taskana/tasks` - List all tasks
- **POST** `/api/taskana/tasks` - Create a new task

#### Example: Create a Task
```bash
curl -X POST http://localhost:8081/api/taskana/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My First Task",
    "description": "This is a test task",
    "workbasketKey": "WBI:81d9d057-bbd6-48db-965f-437802d95a5b",
    "domain": "DOMAIN_A"
  }'
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/tssruntime/
│   │   ├── TssRuntimeApplication.java
│   │   └── controller/
│   │       └── TaskanaController.java
│   └── resources/
│       ├── application.properties
│       ├── drop-taskana-tables.sql
│       ├── taskana-schema.sql
│       └── taskana.properties
└── test/
    ├── java/com/example/tssruntime/
    │   └── TssRuntimeApplicationTests.java
    └── resources/
        └── schema.sql
```

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Verify database credentials in `application.properties`
- Check if the database exists

### Port Already in Use
- Change the port in `application.properties`: `server.port=8081` 