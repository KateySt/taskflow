# Task Management System

This repository contains a **Task Management System** built with **Spring Boot**, designed to facilitate efficient user, project, and task management. The system supports authentication, authorization, CRUD operations, notifications, analytics, and API documentation.

## 🚀 Features

### 🔹 Project Setup
- Spring Boot with essential dependencies:
  - **Spring Web, Spring Data JPA, Spring Security, Spring Mail**
  - **Hibernate, JWT, PostgreSQL/MySQL, Lombok, MapStruct**
  - **Swagger for API documentation**
- Configuration:
  - Database connection via `application.yml`
  - Logging with SLF4J
  - Database migrations using Liquibase

### 🔹 Data Model
#### 🏗️ Entities
- **User**: `id`, `email`, `password`, `name`, `role`, `created_at`
- **Project**: `id`, `name`, `description`, `created_by`, `created_at`
- **Task**: `id`, `title`, `description`, `priority`, `status`, `assigned_to`, `project_id`, `deadline`, `created_at`

#### 🔗 Relationships
- **User → Project**: One-to-many
- **User → Task**: One-to-many
- **Project → Task**: One-to-many

### 🔹 Authentication & Authorization
✅ **Authentication**
- JWT-based authentication
- Endpoints:
  - `POST /auth/login` - Generates a JWT token
  - `POST /auth/register` - Registers a new user with a default role
- Password encryption using `BCryptPasswordEncoder`

🔐 **Authorization**
- **Role-based access control** (Admin, Manager, User)
- Secure endpoints with **Spring Security + JWT validation**

### 🔹 Project Management (CRUD Operations)
#### 📂 **Project Endpoints**
- `POST /projects` - Create a project
- `GET /projects` - Retrieve all projects of the user
- `GET /projects/{id}` - Retrieve project details
- `PUT /projects/{id}` - Update a project
- `DELETE /projects/{id}` - Delete a project

### 🔹 Task Management (CRUD Operations)
#### ✅ **Task Endpoints**
- `POST /tasks` - Create a task
- `GET /tasks` - Retrieve all assigned tasks
- `GET /tasks/{id}` - Retrieve task details
- `PUT /tasks/{id}` - Update a task
- `DELETE /tasks/{id}` - Delete a task

📌 **Features:**
- Task statuses: `new`, `in progress`, `completed`
- Priority levels: `low`, `medium`, `high`

### 🔹 Notifications System
📧 **Email Notifications**
- Task creation alerts
- Deadline reminders (using **Spring Scheduler**)

📡 **Real-Time Notifications**
- WebSocket-based notifications for task updates

### 🔹 Analytics & Reporting
📊 **Analytics Endpoints**
- `GET /projects/{id}/analytics` - Project task statistics
- `GET /users/{id}/analytics` - User task statistics

📈 **Features:**
- SQL-based real-time data aggregation
- Export options: **CSV / Excel**

### 🔹 API Documentation
📌 **Swagger / OpenAPI**
- Automatically generated API documentation using **Springdoc**
- Request & response examples included

### 🔹 Testing & Quality Assurance
✅ **Testing Strategies:**
- **Unit Tests** - Business logic validation with JUnit & Mockito
- **Integration Tests** - REST API tests using MockMvc

### 🔹 Deployment & CI/CD
🚀 **Docker & CI/CD Integration**
- **Dockerfile & docker-compose** for containerization
- **GitHub Actions** for automated CI/CD pipelines

---
