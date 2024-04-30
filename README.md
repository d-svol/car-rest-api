### Car REST Service
***

### Description
*Note that this is a test project (or pet project) intended for demonstration purposes.* \
This is a simple REST API built using Spring Boot, providing users with the following functionalities:

 - Create: Allows users to add new car entries to the system.
 - Read: Enables users to retrieve car information from the database.
 - Update: Permits users to modify existing car records.
 - Delete: Allows users to remove car entries from the system.

***
## Getting Started

1. Start Docker for easy environment setup:
   - This command cleans the project, builds it skipping tests, and starts Docker Compose to launch the containers.

       ```bash
       ./mvnw clean package -DskipTests && docker-compose up 
       ```

   - If you need to stop Docker and remove the application container image, use the following command:

       ```bash
       docker-compose down && docker rmi car-rest-services:latest
       ```
     
2. Explore Endpoints
      - Access Swagger UI at http://localhost:8080/api/v1/swagger-ui/index.html to explore available endpoints and interact with the API.
***

   ![UML Diagram](diagram.png)