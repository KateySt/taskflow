package org.taskflow.com.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.taskflow.com.model.CreateProjectDTO;
import org.taskflow.com.model.ProjectDTO;
import org.taskflow.com.model.UpdateProjectDTO;
import org.taskflow.com.service.ProjectService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private String authHeader;

    @BeforeEach
    public void setUp() {
        authHeader = "Bearer mock_token";
    }

    @Test
    @WithMockUser
    public void createProject_shouldReturnCreatedProject() throws Exception {
        CreateProjectDTO createProjectDTO = new CreateProjectDTO("New Project", "Description of the project");

        when(projectService.createProject(createProjectDTO, authHeader))
                .thenReturn(new ProjectDTO(1L, "New Project", "Description of the project", LocalDateTime.now()));

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader)
                        .content("{\"name\": \"New Project\", \"description\": \"Description of the project\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Project"))
                .andExpect(jsonPath("$.description").value("Description of the project"));
    }

    @Test
    @WithMockUser
    public void getProjects_shouldReturnProjectList() throws Exception {
        List<ProjectDTO> projectList = List.of(new ProjectDTO(1L, "Project 1", "Description 1", LocalDateTime.now()), new ProjectDTO(2L, "Project 2", "Description 2", LocalDateTime.now()));

        when(projectService.getProjects(authHeader))
                .thenReturn(projectList);

        mockMvc.perform(get("/api/v1/projects")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Project 1"))
                .andExpect(jsonPath("$[1].name").value("Project 2"));
    }

    @Test
    @WithMockUser
    public void getProjectById_shouldReturnProject() throws Exception {
        ProjectDTO project = new ProjectDTO(1L, "Project 1", "Description 1", LocalDateTime.now());

        when(projectService.getProjectById(1L, authHeader))
                .thenReturn(project);

        mockMvc.perform(get("/api/v1/projects/{id}", 1L)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Project 1"))
                .andExpect(jsonPath("$.description").value("Description 1"));
    }

    @Test
    @WithMockUser
    public void updateProject_shouldReturnUpdatedProject() throws Exception {
        UpdateProjectDTO updateProjectDTO = new UpdateProjectDTO("Updated Project", "Updated Description");

        when(projectService.updateProject(1L, updateProjectDTO, authHeader))
                .thenReturn(new ProjectDTO(1L, "Updated Project", "Updated Description", LocalDateTime.now()));

        mockMvc.perform(put("/api/v1/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader)
                        .content("{\"name\": \"Updated Project\", \"description\": \"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    @WithMockUser
    public void deleteProject_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/projects/{id}", 1L)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk());
    }
}
