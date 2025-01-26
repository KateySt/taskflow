package org.taskflow.com.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.taskflow.com.entity.ProjectEntity;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.model.CreateProjectDTO;
import org.taskflow.com.model.ProjectDTO;
import org.taskflow.com.model.UpdateProjectDTO;
import org.taskflow.com.repository.ProjectRepository;
import org.taskflow.com.repository.UserRepository;
import org.taskflow.com.annotation.CheckToken;
import org.taskflow.com.service.ProjectService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TokenCacheServiceImpl tokenCacheService;

    /**
     * Creates a new project and returns the details of the newly created project.
     * @param createProjectDTO The DTO containing project creation details.
     * @param authHeader The authentication header containing the user's token.
     * @return The ProjectDTO containing the created project information.
     */
    @CheckToken
    public ProjectDTO createProject(CreateProjectDTO createProjectDTO, String authHeader) {
        // Get the user based on the provided token
        UserEntity user = userRepository.findByEmail(tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Build a new project entity with the provided data
        ProjectEntity project = ProjectEntity.builder()
                .name(createProjectDTO.name())
                .description(createProjectDTO.description())
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();

        // Save the project to the database
        ProjectEntity savedProject = projectRepository.save(project);

        // Return the project DTO
        return toProjectDTO(savedProject);
    }

    /**
     * Retrieves all projects created by the authenticated user.
     * @param authHeader The authentication header containing the user's token.
     * @return A list of ProjectDTOs representing the user's projects.
     */
    public List<ProjectDTO> getProjects(String authHeader) {
        // Find all projects created by the user using the email from the token
        return projectRepository.findByCreatedBy_Email(tokenCacheService.getEmailByToken(authHeader))
                .stream()
                .map(this::toProjectDTO) // Map each project to a ProjectDTO
                .toList();
    }

    /**
     * Retrieves a specific project by its ID for the authenticated user.
     * @param projectId The ID of the project to retrieve.
     * @param authHeader The authentication header containing the user's token.
     * @return The ProjectDTO representing the project.
     */
    @CheckToken
    public ProjectDTO getProjectById(Long projectId, String authHeader) {
        // Find the project by ID and ensure it belongs to the authenticated user
        ProjectEntity project = projectRepository.findByIdAndCreatedBy_Email(projectId, tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Return the project DTO
        return toProjectDTO(project);
    }

    /**
     * Updates an existing project with new details.
     * @param projectId The ID of the project to update.
     * @param updateProjectDTO The DTO containing updated project details.
     * @param authHeader The authentication header containing the user's token.
     * @return The updated ProjectDTO representing the project after the update.
     */
    @CheckToken
    public ProjectDTO updateProject(Long projectId, UpdateProjectDTO updateProjectDTO, String authHeader) {
        // Find the project by ID and ensure it belongs to the authenticated user
        ProjectEntity project = projectRepository.findByIdAndCreatedBy_Email(projectId, tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Update the project details with the provided data
        project.setName(updateProjectDTO.name());
        project.setDescription(updateProjectDTO.description());

        // Save the updated project
        ProjectEntity updatedProject = projectRepository.save(project);

        // Return the updated project DTO
        return toProjectDTO(updatedProject);
    }

    /**
     * Deletes a specific project by its ID.
     * @param projectId The ID of the project to delete.
     * @param authHeader The authentication header containing the user's token.
     */
    @CheckToken
    public void deleteProject(Long projectId, String authHeader) {
        // Find the project by ID and ensure it belongs to the authenticated user
        ProjectEntity project = projectRepository.findByIdAndCreatedBy_Email(projectId, tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Delete the project from the repository
        projectRepository.delete(project);
    }

    /**
     * Converts a ProjectEntity object to a ProjectDTO.
     * @param project The ProjectEntity to convert.
     * @return The ProjectDTO representing the project.
     */
    private ProjectDTO toProjectDTO(ProjectEntity project) {
        return new ProjectDTO(project.getId(), project.getName(), project.getDescription(), project.getCreatedAt());
    }
}
