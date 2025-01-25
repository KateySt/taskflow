package org.taskflow.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.model.CreateProjectDTO;
import org.taskflow.com.model.ProjectDTO;
import org.taskflow.com.model.UpdateProjectDTO;
import org.taskflow.com.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@AllArgsConstructor
@Slf4j
@Tag(name = "Projects", description = "Endpoints for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(
            summary = "Create a new project",
            description = "Allows authenticated users to create a new project.",
            tags = {"Projects"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Project created successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error. The input is invalid."
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDTO createProject(
            @RequestBody @Valid CreateProjectDTO createProjectDTO,
            @RequestHeader("Authorization") String authHeader) {
        log.info("User is creating a new project: {}",  createProjectDTO);
        return projectService.createProject(createProjectDTO,authHeader);
    }

    @Operation(
            summary = "Get all projects",
            description = "Retrieve all projects created by the authenticated user.",
            tags = {"Projects"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of projects retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectDTO.class)
                    )
            )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectDTO> getProjects(@RequestHeader("Authorization") String authHeader) {
        return projectService.getProjects(authHeader);
    }

    @Operation(
            summary = "Get project by ID",
            description = "Retrieve a specific project by its ID, if it belongs to the authenticated user.",
            tags = {"Projects"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Project retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Project not found."
            )
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO getProjectById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user,
            @RequestHeader("Authorization") String authHeader) {
        log.info("Fetching project {} for user", id);
        return projectService.getProjectById(id,authHeader);
    }

    @Operation(
            summary = "Update project",
            description = "Update the details of an existing project.",
            tags = {"Projects"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Project updated successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Project not found."
            )
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO updateProject(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProjectDTO updateProjectDTO,
            @RequestHeader("Authorization") String authHeader) {
        log.info("Updating project {} for user: {}", id, updateProjectDTO);
        return projectService.updateProject(id, updateProjectDTO,authHeader);
    }

    @Operation(
            summary = "Delete project",
            description = "Delete a specific project by its ID.",
            tags = {"Projects"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Project deleted successfully."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Project not found."
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        log.info("Deleting project {} for user", id);
        projectService.deleteProject(id,authHeader);
    }
}
