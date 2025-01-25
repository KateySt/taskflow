package org.taskflow.com.service;

import org.taskflow.com.model.CreateProjectDTO;
import org.taskflow.com.model.ProjectDTO;
import org.taskflow.com.model.UpdateProjectDTO;

import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(CreateProjectDTO createProjectDTO, String authHeader);

    List<ProjectDTO> getProjects(String authHeader);

    ProjectDTO getProjectById(Long projectId, String authHeader);

    ProjectDTO updateProject(Long projectId, UpdateProjectDTO updateProjectDTO, String authHeader);

    void deleteProject(Long projectId, String authHeader);
}
