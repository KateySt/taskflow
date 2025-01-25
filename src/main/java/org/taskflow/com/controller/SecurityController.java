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
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.taskflow.com.model.*;
import org.taskflow.com.service.SecurityService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@Tag(name = "Security", description = "Endpoints for user authentication and registration")
public class SecurityController {

    private final SecurityService service;

    /**
     * Logs in an existing user and returns session information, including a JWT token.
     */
    @Operation(
            summary = "User Login",
            description = "Authenticate an existing user and obtain a session token.",
            tags = {"Security"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful. Session information returned.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SessionInfo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. Invalid credentials provided."
            )
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SessionInfo login(Authentication auth) {
        log.info("User attempting to log in: {}", auth.getName());
        return service.loginInfo(auth);
    }

    /**
     * Registers a new user and returns session information, including a JWT token.
     */
    @Operation(
            summary = "User Registration",
            description = "Register a new user by providing necessary information.",
            tags = {"Security"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Registration successful. Session information returned.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SessionInfo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error. Provided input is invalid."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict. The email address is already in use."
            )
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionInfo register(@Valid @RequestBody NewUser newUser) {
        log.info("Registering new user: {}", newUser.email());
        return service.register(newUser);
    }
}
