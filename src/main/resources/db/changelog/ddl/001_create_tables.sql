-- liquibase formatted sql
-- changeset kate:1

CREATE TABLE users (
                       id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);


CREATE TABLE projects (
                          id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          created_by BIGINT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_projects_created_by FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE tasks (
                       id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       priority VARCHAR(50) NOT NULL,
                       status VARCHAR(50) NOT NULL,
                       assigned_to BIGINT,
                       project_id BIGINT NOT NULL,
                       deadline TIMESTAMP,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_tasks_assigned_to FOREIGN KEY (assigned_to) REFERENCES users (id),
                       CONSTRAINT fk_tasks_project_id FOREIGN KEY (project_id) REFERENCES projects (id)
);
