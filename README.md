# Busash Backend Application

A Spring Boot application, built using Kotlin, Springfox for Swagger UI, and MapStruct for simple object mappings.

## System Requirements

- JDK 17
- Maven

## Instructions to Start the Application

1. Navigate to the main directory of the project:

   ```bash
   cd busash-be-main 
   ```

2. Compile the MapStruct classes:

   ```bash
   mvn compile 
   ```

3. Start the application using Maven:

   ```bash
   mvn spring-boot:run 
   ```

The application is now running at `http://localhost:8080/`

## Accessing the Swagger UI

After starting the application, open a web browser and navigate to:

`http://localhost:8080/swagger-ui/`

Here you will find the documentation for the application's API endpoints.
