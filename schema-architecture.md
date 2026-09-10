# Architecture

## Architecture Summary

This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.

## Numbered flow of Data and Control

1. User accesses AdminDashboard or Appointment pages.
2. The action is routed to the appropriate Thymeleaf or REST controller.
3. The controller calls the service layer, which applies business logic and validations. Additionally, it coordinates workflows across multiple entities and ensures a clean separation between controller logic and data access.
4. The service layer communicates with the repository layer. There are two type of repositories: MySQL and MongoDB. MySQL uses Spring Data JPA and MongoDB uses Spring Data MongoDB.
5. Each repository interfaces with an underlying database engine. MySQL stores all core entities that benefit from a normalized relational schema and constraints—such as users, roles, and appointments. MongoDB stores flexible and nested data structures, such as prescriptions, which may vary in format and allow for rapid schema evolution.
6. Once data is retrieved from the database, it is mapped into Java model classes that the application can work with. This process is known as model binding.In the case of MySQL, data is converted into JPA entities, which represent rows in relational tables and are annotated with @Entity. For MongoDB, data is loaded into document objects, typically annotated with @Document, which map to BSON/JSON structures in collections.
7. Finally, the bound models are used in the response layer: In MVC flows, models are passed from the controller to Thymeleaf templates, where they are rendered as dynamic HTML for the browser. In REST flows, the same models (or transformed DTOs) are serialized into JSON and sent back to the client as part of an HTTP response.