package org.taskflow.com.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.taskflow.com.entity.ProjectEntity;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.mapper.MapperProject;
import org.taskflow.com.model.CreateProjectDTO;
import org.taskflow.com.model.ProjectDTO;
import org.taskflow.com.model.UpdateProjectDTO;
import org.taskflow.com.repository.ProjectRepository;
import org.taskflow.com.repository.UserRepository;
import org.taskflow.com.service.TokenCacheService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenCacheService tokenCacheService;
    @Mock
    private MapperProject mapperProject;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private UserEntity userEntity;
    private ProjectEntity projectEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("user@example.com");

        projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        projectEntity.setName("Project 1");
        projectEntity.setDescription("Description");
        projectEntity.setCreatedAt(LocalDateTime.now());
    }


    @Test
    void createProject_shouldReturnProjectDTO_whenValidData() {
        String authHeader = "validToken";
        CreateProjectDTO createProjectDTO = new CreateProjectDTO("Project 1", "Description");
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(userEntity));
        when(projectRepository.save(projectEntity)).thenReturn(projectEntity);
        when(mapperProject.toProjectEntity(createProjectDTO, userEntity)).thenReturn(projectEntity);
        when(mapperProject.toProjectDTO(projectEntity)).thenReturn(new ProjectDTO(1L, "Project 1", "Description", LocalDateTime.now()));

        ProjectDTO result = projectService.createProject(createProjectDTO, authHeader);

        assertNotNull(result);
        assertEquals("Project 1", result.name());
        assertEquals("Description", result.description());
        verify(projectRepository).save(any(ProjectEntity.class));
    }

    @Test
    void createProject_shouldThrowException_whenUserNotFound() {
        String authHeader = "invalidToken";
        CreateProjectDTO createProjectDTO = new CreateProjectDTO("Project 1", "Description");
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.createProject(createProjectDTO, authHeader));
    }

    @Test
    void getProjects_shouldReturnListOfProjects() {
        String authHeader = "validToken";
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn("user@example.com");
        when(projectRepository.findByCreatedBy_Email("user@example.com"))
                .thenReturn(List.of(projectEntity));
        when(mapperProject.toProjectDTO(projectEntity)).thenReturn(new ProjectDTO(1L, "Project 1", "Description", null));

        List<ProjectDTO> result = projectService.getProjects(authHeader);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Project 1", result.get(0).name());
    }

    @Test
    void getProjectById_shouldReturnProjectDTO_whenProjectFound() {
        String authHeader = "validToken";
        Long projectId = 1L;
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn("user@example.com");
        when(projectRepository.findByIdAndCreatedBy_Email(projectId, "user@example.com"))
                .thenReturn(Optional.of(projectEntity));
        when(mapperProject.toProjectDTO(projectEntity)).thenReturn(new ProjectDTO(1L, "Project 1", "Description", null));

        ProjectDTO result = projectService.getProjectById(projectId, authHeader);

        assertNotNull(result);
        assertEquals("Project 1", result.name());
    }

    @Test
    void getProjectById_shouldThrowException_whenProjectNotFound() {
        String authHeader = "validToken";
        Long projectId = 1L;
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn("user@example.com");
        when(projectRepository.findByIdAndCreatedBy_Email(projectId, "user@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.getProjectById(projectId, authHeader));
    }

    @Test
    void updateProject_shouldReturnUpdatedProjectDTO() {
        String authHeader = "validToken";
        Long projectId = 1L;
        UpdateProjectDTO updateProjectDTO = new UpdateProjectDTO("Updated Project", "Updated Description");
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn("user@example.com");
        when(projectRepository.findByIdAndCreatedBy_Email(projectId, "user@example.com"))
                .thenReturn(Optional.of(projectEntity));
        projectEntity.setName("Updated Project");
        projectEntity.setDescription("Updated Description");
        when(projectRepository.save(projectEntity)).thenReturn(projectEntity);
        when(mapperProject.toProjectDTO(projectEntity)).thenReturn(new ProjectDTO(1L, projectEntity.getName(), projectEntity.getDescription(), LocalDateTime.now()));

        ProjectDTO result = projectService.updateProject(projectId, updateProjectDTO, authHeader);

        assertNotNull(result);
        assertEquals("Updated Project", result.name());
        assertEquals("Updated Description", result.description());
    }

    @Test
    void deleteProject_shouldDeleteProject() {
        String authHeader = "validToken";
        Long projectId = 1L;
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn("user@example.com");
        when(projectRepository.findByIdAndCreatedBy_Email(projectId, "user@example.com"))
                .thenReturn(Optional.of(projectEntity));

        projectService.deleteProject(projectId, authHeader);

        verify(projectRepository).delete(projectEntity);
    }
}
