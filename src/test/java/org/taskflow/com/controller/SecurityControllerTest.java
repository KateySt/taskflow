package org.taskflow.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.exception.EmailAlreadyOccupiedException;
import org.taskflow.com.model.NewUser;
import org.taskflow.com.model.SessionInfo;
import org.taskflow.com.service.SecurityService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SecurityController.class)
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SecurityService securityService;

    @Mock
    private Authentication auth;

    private String authHeader;

    private UserEntity user;

    @BeforeEach
    public void setup() {

        user = UserEntity.builder()
                .id(1L)
                .name("Jon Snow")
                .email("user@gmail.com")
                .password("Password123$")
                .build();

        authHeader = "Bearer testToken";
    }

    @Test
    void register_shouldReturnSessionInfo() throws Exception {
        NewUser newUser = new NewUser("John Doe", "user@gmail.com", "Password123$");

        SessionInfo sessionInfo = new SessionInfo(authHeader);
        when(securityService.register(any(NewUser.class))).thenReturn(sessionInfo);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated());
    }

    @Test
    void register_shouldReturnBadRequest_whenValidationError() throws Exception {
        NewUser invalidUser = new NewUser("invalid-email", "Password123$", "User Name");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnConflict_whenEmailAlreadyExists() throws Exception {
        NewUser existingUser = new NewUser("user@gmail.com", "Password123$", "User Name");

        when(securityService.register(any(NewUser.class)))
                .thenThrow(new EmailAlreadyOccupiedException("Email already in use"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isBadRequest());
    }
}
