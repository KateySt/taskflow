package org.taskflow.com.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.enums.Role;
import org.taskflow.com.exception.EmailAlreadyOccupiedException;
import org.taskflow.com.mapper.MapperSecurity;
import org.taskflow.com.model.NewUser;
import org.taskflow.com.model.SessionInfo;
import org.taskflow.com.repository.UserRepository;
import org.taskflow.com.service.TokenCacheService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private UserRepository repository;

    @Mock
    private MapperSecurity mapperSecurity;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenCacheService tokenCacheService;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private UserEntity user;
    private UserDetailsImpl userDetails;
    private NewUser newUser;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .password("hashedPassword")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        userDetails = new UserDetailsImpl(user.getEmail(), user.getPassword(), user.getRole());

        newUser = new NewUser("Test User", "test@example.com", "password");
    }

    @Test
    void loginInfo_ShouldReturnSessionInfo() {
        var auth = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        var sessionInfo = new SessionInfo("jwt-token");

        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(mapperSecurity.toUserDetailsImpl(user)).thenReturn(userDetails);
        when(mapperSecurity.toSessionInfo("jwt-token")).thenReturn(sessionInfo);

        Jwt dummyJwt = Jwt.withTokenValue("jwt-token")
                .header("alg", "none")
                .claim("sub", "test@example.com")
                .build();
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(dummyJwt);

        var result = securityService.loginInfo(auth);

        assertEquals("jwt-token", result.token());
        verify(tokenCacheService).saveToken("test@example.com", "Bearer jwt-token");
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        when(repository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyOccupiedException.class, () -> securityService.register(newUser));
        verify(repository, never()).save(any());
    }

    @Test
    void createScope_ShouldReturnUserRolesAsString() {
        var userDetailsWithRoles = new UserDetailsImpl(user.getEmail(), user.getPassword(), Role.ADMIN);

        var scope = securityService.createScope(userDetailsWithRoles);

        assertEquals(Role.ADMIN.getAuthority(), scope);
    }
}
