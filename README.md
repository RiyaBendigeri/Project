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

To switch to MySQL:(if mysql workbench with credentials present)

Comment out H2 dependency and uncomment MySQL dependency in pom.xml:  

Comment out H2 code in application.properties and uncomment MySQL




## Tables and Endpoints
### Categories(table):  

Schema:  

Category ID  (Auto-increment)

Category name

### API Endpoints
GET /api/categories - Get all categories  

GET /api/categories/{id} - Get category by ID  

POST /api/categories - Create new category  

PATCH /api/categories/{id} - Update category  

DELETE /api/categories/{id} - Delete category  

### Products(table):  
Schema:  
Product ID  (Auto-increment)  
Product name  
Product price  
Product catID (foreign key->categories Category ID)  

### API Endpoints
GET /api/products - Get all products  

GET /api/products/{id} - Get product by ID  
 
POST /api/products - Create new product  

PATCH /api/products/{id} - Update product  

DELETE /api/products/{id} - Delete product  



## Setup Instructions (IntelliJ IDEA)

1. Clone the repository
    - Open IntelliJ IDEA
    - Go to `File` → `New` → `Project from Version Control`
    - URL: [repo-url]

2. Configure JDK
    - Go to `File` → `Project Structure`
    - Set Project SDK to Java 17 or higher
    - Click `Apply`

3. Load Maven Changes
    - Right-click `pom.xml`
    - Select `Maven` → `Reload Project`

4. Run Application
    - Open `src/main/java/org/example/Main.java`
    - Right-click → `Run 'Main.main()'`

5. Verify
    - Application runs on: http://localhost:8080
    - H2 Console: http://localhost:8080/h2-console
    - H2 Console credentials:
        - JDBC URL: jdbc:h2:mem:testdb
        - Username: sa
        - Password: [leave blank]
    - Can test on postman

## Test Coverage  

The application includes comprehensive tests for:  

Category CRUD operations  
Product CRUD operations  

1.Error handling  
2.Edge cases  
3.Data validation  
## Running Tests

1. Run all tests:
    - Right-click on `src/test` folder
    - Select `Run 'All Tests'`
    - Or click on the run icon
   

2. Run specific test class:
    - Right-click on test class
    - Select `Run 'TestClassName'`
    - Click on the run icon for each test
   
## Data Validation:  

### Categories  
1.Name cannot be empty  
2.Duplicate names not allowed  
3.Url error not allowed  
4.Any missing parameters in the url not allowed  
5.Id insertion is automatic , hence if ID specified not allowed while post request


### Products  
1.Name cannot be empty  
2.Price must be positive  
3.Category ID must exist  
4.Duplicate names not allowed    
5.Any missing parameters in the url not allowed  
6.Id insertion is automatic , hence if ID specified not allowed while post request
 

## Technologies Used  
Spring Boot  
Spring Data JPA  
H2 Database/MySQL  
JUnit  for testing  
Maven    


## Requirements  
Intellij IDE  
Java 17 or higher  
Maven 4.0.0 or higher  
MySQL (if using MySQL configuration)
