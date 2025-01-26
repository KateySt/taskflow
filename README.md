# Task Management System

This repository contains a Task Management System built with Spring Boot, designed to manage users, projects, and tasks efficiently. The project implements a full-stack backend solution, including authentication, authorization, CRUD operations, notifications, analytics, and API documentation.

---

## Features Overview

1. **Project Initialization**
    - Maven/Gradle project with essential dependencies:
        - Spring Boot (Web, Data JPA, Security, Mail).
        - Hibernate, JWT, PostgreSQL/MySQL, Lombok, MapStruct.
        - Swagger for API documentation.
    - Configurations:
        - Database connection (`application.yml` or `application.properties`).
        - Logging setup with Logback/SLF4J.
    - Database integration and migration using Flyway or Liquibase.

2. **Data Model Development**
    - **Entities:**
        - `User`: id, email, password, name, role, created_at.
        - `Project`: id, name, description, created_by, created_at.
        - `Task`: id, title, description, priority, status, assigned_to, project_id, deadline, created_at.
    - **Relationships:**
        - User → Project: one-to-many.
        - User → Task: one-to-many.
        - Project → Task: one-to-many.

3. **Authentication & Authorization**
    - **Authentication:**
        - `/auth/login` endpoint for JWT token generation.
        - `/auth/register` endpoint for user registration with default roles.
        - Password encryption using `BCryptPasswordEncoder`.
    - **Authorization:**
        - Role-based access control (Admin, Manager, User).
        - Secure endpoints with Spring Security and JWT validation.

4. **CRUD for Projects**
    - **Endpoints:**
        - `POST /projects` - Create a project.
        - `GET /projects` - Retrieve all projects for the current user.
        - `GET /projects/{id}` - Retrieve project details.
        - `PUT /projects/{id}` - Update a project.
        - `DELETE /projects/{id}` - Delete a project.
    - **Features:**
        - DTO-based request and response models.
        - Access control ensuring users can manage their projects only.

5. **CRUD for Tasks**
    - **Endpoints:**
        - `POST /tasks` - Create a task.
        - `GET /tasks` - Retrieve all tasks assigned to the user.
        - `GET /tasks/{id}` - Retrieve task details.
        - `PUT /tasks/{id}` - Update a task.
        - `DELETE /tasks/{id}` - Delete a task.
    - **Features:**
        - Support for task statuses (new, in progress, completed).
        - Priority management (low, medium, high).

6. **Notifications**
    - **Email Notifications:**
        - Task creation alerts.
        - Deadline reminders (using Spring Scheduler).
    - **Real-Time Notifications:**
        - Implemented via WebSocket or Server-Sent Events (SSE).
        - Notifications for task status updates.

7. **Analytics Integration**
    - **Endpoints:**
        - `GET /projects/{id}/analytics` - Project task statistics.
        - `GET /users/{id}/analytics` - User task statistics.
    - **Features:**
        - SQL queries for real-time data aggregation.
        - Optional export to CSV/Excel.

8. **API Documentation**
    - **Swagger/OpenAPI:**
        - Automatically generated API documentation using Springdoc.
        - Examples of requests and responses included.

9. **Testing**
    - **Unit Tests:** Service logic validation using JUnit and Mockito.
    - **Integration Tests:** REST endpoints tested with MockMvc.
    - **Security Tests:** Validation of access control and token authentication.

10. **Deployment**
    - **Docker & CI/CD:**
        - `Dockerfile` and `docker-compose` for containerization.
        - CI/CD pipelines via GitHub Actions or Jenkins.
    - Deployment to cloud platforms like Heroku, AWS, or DigitalOcean.

---