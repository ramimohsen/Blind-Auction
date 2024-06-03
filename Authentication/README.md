# Authentication Service

This service is responsible for handling user authentication in the application.

## Technologies Used

- Java 17
- Spring Boot 3.2.6
- Maven
- H2 Database In-Memory

## Database

The service uses an H2 in-memory database for storing user credentials.

## API Documentation

The API documentation for this service is available via Swagger UI. You can access it at the following
URL: `http://localhost:8080/swagger-ui.html`

## Error Handling

The service includes comprehensive error handling. For example, it handles cases where a user already exists, where
authentication fails, and where there are runtime exceptions.

## Running the Service

To run the service, use the following command: `mvn spring-boot:run`

This will start the service on port 8080.

## Endpoints

The service has the following endpoints:

- POST /auth/register
- POST /auth/login
- GET /auth/validate

## Testing

The service includes unit tests for the controller and service layers. You can run the tests using the following
command: `mvn test`

## Contact

If you have any questions or issues, please open an issue in the GitHub repository.
