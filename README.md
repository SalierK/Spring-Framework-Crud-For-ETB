# Spring Boot CRUD Application with Enhanced Security

This project is a Spring Boot CRUD application that has been secured with comprehensive security measures. The entire project is dockerized and can be run either through IntelliJ or Docker.

## Security Improvements

This application has been enhanced with the following security measures:

### 🔒 **Authentication & Authorization**
- **BCrypt Password Encoding**: Replaced plain text passwords with secure BCrypt hashing
- **Role-Based Access Control**: Implemented with `@PreAuthorize` annotations
  - ADMIN role: Full CRUD operations
  - PERSON role: Read-only access
- **Method Security**: Enabled Spring Security method-level authorization

### 🛡️ **CSRF Protection**
- **Enabled CSRF Protection**: Properly configured with cookie-based CSRF tokens
- **Selective Disabling**: CSRF only disabled for H2 console (development use)

### 🔐 **Input Validation**
- **Comprehensive Validation**: Added Jakarta validation annotations to all model fields
- **Size Limits**: Enforced maximum lengths for all string fields
- **Required Fields**: Validation for mandatory fields
- **Global Exception Handling**: Proper error responses for validation failures

### 🏗️ **Dependency Security**
- **Updated Dependencies**: Removed incompatible old Spring Security version
- **Removed Deprecated APIs**: Eliminated javax.servlet dependencies
- **Modern Spring Security**: Using Spring Security 6.x compatible configuration

### 🔧 **Infrastructure Security**
- **H2 Console**: Restricted to ADMIN role only in development
- **Frame Options**: Configured for same-origin policy
- **Secure Headers**: Implemented security headers configuration

## User Credentials

Two types of users are available:

| Feature  | Admin User       | Person User      |
|----------|------------------|------------------|
| Username | admin            | person           |
| Password | adminpassword    | personpassword   |
| Role     | ADMIN            | PERSON           |
| Create   | ✅               | ❌               |
| Read     | ✅               | ✅               |
| Update   | ✅               | ❌               |
| Delete   | ✅               | ❌               |

> **Note**: These credentials are stored with BCrypt encryption. Access the `CustomUserDetailsService` class in the code for implementation details.

## Running the Project

### Docker Build

1. Build with Maven:
   ```bash
   mvn clean install -DskipTests
   ```
   > **Note**: Remove `-DskipTests` if you want to run tests during Docker build.

2. Check JDK version if you encounter Lombok errors:
   - Project developed with Oracle Open JDK 17
   - JDK 21 and 23 may have version compatibility issues

3. Start Docker containers:
   - Windows: `Docker Compose Up`
   - macOS: `docker-compose up`

4. Verify the project is running:
   - Access `localhost:8080/api/tasks` in your browser
   - If you see the Spring Security login page, the project is running successfully

### IntelliJ Build

1. Start only PostgreSQL:
   ```bash
   docker-compose up postgres
   ```
   > **Note**: The java_app image should be down or not compiled. Check with `docker ps -a`.

2. In IntelliJ:
   - Open the project
   - Navigate to `src/main/java/com.codecraft.Crud_app/CrudAppApplication`
   - Click the run button at the top
   - If you encounter errors, check your JDK version in Run/Debug Configurations

## Testing with Postman

### API Operations

All requests are made to `localhost:8080/api/tasks`.

#### CREATE Operation
- Method: POST
- Authentication: Basic Auth
- Body (raw JSON):
  ```json
  {
      "title": "Sample Task",
      "description": "Task description",
      "asigneedTo": "John Doe",
      "status": 1
  }
  ```
  > **Note**: Status must be between 0-2 (validated)

#### READ Operation
- Method: GET
- URL: `localhost:8080/api/tasks`
- Authentication: Basic Auth

#### UPDATE Operation
- Method: PUT
- URL: `localhost:8080/api/tasks/{id}`
- Authentication: Basic Auth
- Body: Send updated fields in JSON format

#### DELETE Operation
- Method: DELETE
- URL: `localhost:8080/api/tasks/{id}`
- Authentication: Basic Auth

## Field Validation Rules

- **Title**: Required, max 200 characters
- **Assigned To**: Required, max 100 characters  
- **Description**: Optional, max 1000 characters
- **Status**: Required, integer between 0-2

## Development & Testing

- **Unit Tests**: 6 comprehensive tests covering all CRUD operations
- **Test Database**: H2 in-memory database for isolated testing
- **Security Testing**: Tests include proper role-based access control with `@WithMockUser`

## Error Handling

The application includes a global exception handler that provides meaningful error messages for:
- Validation failures
- Constraint violations
- Authentication errors
- Authorization failures
