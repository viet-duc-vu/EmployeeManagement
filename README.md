# Employee Management Application

## Overview
Employee management is the RESTful backend part of the management application that helps keep track of information of employees in an organization. The front end part is implemented in React.js and can be found [here](https://github.com/viet-duc-vu/employee-management-frontend).

## Technologies Used
- Java 17
- Spring Boot
- MySQL
- Maven

## How to run
```bash
git clone https://github.com/viet-duc-vu/employee-management.git
cd employee-management
./mvnw package
java -jar target/*.jar
```
Or you can run directly from Maven using the Spring Boot Maven plugin. By default, the app uses the in-memory database
H2 which gets populated at startup with some initial data. To run with MySQL, using the flag `-Dspring-boot.run.profiles=prod`, as below:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```
You can also use the provided `docker-compose.yml` file to run the app inside a Docker container:
```bash
docker compose -f docker-compose.yml up --build
```

## Testing the API endpoints with `curl`
List all employees
```bash
curl --request GET --url http://localhost:8080/api/v1/employees
```


