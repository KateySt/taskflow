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
import org.taskflow.com.service.CheckToken;
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

    @CheckToken
    public ProjectDTO createProject(CreateProjectDTO createProjectDTO, String authHeader) {
        UserEntity user = userRepository.findByEmail(tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ProjectEntity project = ProjectEntity.builder()
                .name(createProjectDTO.name())
                .description(createProjectDTO.description())
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();

        ProjectEntity savedProject = projectRepository.save(project);

        return toProjectDTO(savedProject);
    }

    public List<ProjectDTO> getProjects(String authHeader) {
        return projectRepository.findByCreatedBy_Email(tokenCacheService.getEmailByToken(authHeader))
                .stream()
                .map(this::toProjectDTO)
                .toList();
    }

    @CheckToken
    public ProjectDTO getProjectById(Long projectId, String authHeader) {
        ProjectEntity project = projectRepository.findByIdAndCreatedBy_Email(projectId, tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        return toProjectDTO(project);
    }

    @CheckToken
    public ProjectDTO updateProject(Long projectId, UpdateProjectDTO updateProjectDTO, String authHeader) {
        ProjectEntity project = projectRepository.findByIdAndCreatedBy_Email(projectId, tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        project.setName(updateProjectDTO.name());
        project.setDescription(updateProjectDTO.description());
        ProjectEntity updatedProject = projectRepository.save(project);

        return toProjectDTO(updatedProject);
    }

    @CheckToken
    public void deleteProject(Long projectId, String authHeader) {
        ProjectEntity project = projectRepository.findByIdAndCreatedBy_Email(projectId, tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        projectRepository.delete(project);
    }

    private ProjectDTO toProjectDTO(ProjectEntity project) {
        return new ProjectDTO(project.getId(), project.getName(), project.getDescription(), project.getCreatedAt());
    }
}