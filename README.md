# Spring Boot CRUD Application with Categories and Products

This Spring Boot application demonstrates CRUD operations for Categories and Products with support for both MySQL and H2 databases.

## Features

- CRUD operations for Categories
- CRUD operations for Products
- Database flexibility (H2/MySQL)
- RESTful API endpoints

## Database Configuration

### H2 Database (Default - In-memory)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
MySQL Configuration
To switch to MySQL:

Comment out H2 dependency and uncomment MySQL dependency in pom.xml:
Comment out H2 code in application.properties and uncomment MySQL



API Endpoints
Categories
GET /api/categories - Get all categories
GET /api/categories/{id} - Get category by ID
POST /api/categories - Create new category
PATCH /api/categories/{id} - Update category
DELETE /api/categories/{id} - Delete category
Products
GET /api/products - Get all products
GET /api/products/{id} - Get product by ID
POST /api/products - Create new product
PATCH /api/products/{id} - Update product
DELETE /api/products/{id} - Delete product


Running the Application
Clone the repository
```
git clone [repository-url]
```
Navigate to project directory
```
cd [project-directory]
```
Build the project
```
mvn clean install
```
Run the application
```
mvn spring-boot:run
```
Running Tests
```
mvn test
```
Run specific test class:
```
mvn test -Dtest=CategoryControllerTest
mvn test -Dtest=ProductsControllerTest
```
Test Coverage
The application includes comprehensive tests for:

Category CRUD operations
Product CRUD operations
Error handling
Edge cases
Data validation
Data Validation
Categories
Name cannot be empty
Duplicate names not allowed
Invalid IDs handled appropriately
Products
Name cannot be empty
Price must be positive
Category ID must exist
Duplicate names not allowed
H2 Console Access
When using H2:

Access H2 console at: http://localhost:8080/h2-console
Use these settings:
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: [leave blank]
Technologies Used
Spring Boot
Spring Data JPA
H2 Database/MySQL
JUnit
MockMvc for testing
Maven
Requirements
Java 17 or higher
Maven 3.6 or higher
MySQL (if using MySQL configuration)
