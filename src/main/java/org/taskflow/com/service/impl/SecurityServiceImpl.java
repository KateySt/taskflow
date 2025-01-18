package org.taskflow.com.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.enums.Role;
import org.taskflow.com.exception.EmailAlreadyOccupiedException;
import org.taskflow.com.mapper.MapperSecurity;
import org.taskflow.com.model.NewUser;
import org.taskflow.com.model.SessionInfo;
import org.taskflow.com.repository.UserRepository;
import org.taskflow.com.service.SecurityServiceInterface;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Service
@Transactional
public class SecurityServiceImpl implements SecurityServiceInterface {

    private final JwtEncoder jwtEncoder;
    private final UserRepository repository;
    private final MapperSecurity mapperSecurity;
    private final PasswordEncoder passwordEncoder;

    /**
     * Handles login by retrieving the user, generating a JWT token,
     * and mapping it to the session information.
     */
    @Override
    public SessionInfo loginInfo(Authentication auth) {
        var user = repository.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException(auth.getName() + " not found user by email"));

        var token = getJWTToken(mapperSecurity.toUserDetailsImpl(user), user.getId());
        return mapperSecurity.toSessionInfo(token);
    }

    /**
     * Registers a new user, ensures the email is not already taken,
     * saves the user, generates a JWT token, and maps it to session information.
     */
    @Override
    public SessionInfo register(NewUser newUser) {
        var user = saveNewUser(newUser);
        var token = getJWTToken(mapperSecurity.toUserDetailsImpl(user), user.getId());
        return mapperSecurity.toSessionInfo(token);
    }

    /**
     * Saves a new user to the database after validating that the email is unique.
     *
     * @throws EmailAlreadyOccupiedException if the email already exists in the repository.
     */
    private UserEntity saveNewUser(NewUser newUser) {
        if (repository.existsByEmail(newUser.email())) {
            throw new EmailAlreadyOccupiedException(newUser.email());
        }
        return repository.save(UserEntity.builder()
                .name(newUser.fullName())
                .email(newUser.email())
                .password(passwordEncoder.encode(newUser.password()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build());
    }

    /**
     * Retrieves the user ID based on the provided email.
     *
     * @throws UsernameNotFoundException if no user is found with the given email.
     */
    private String getUserIdByEmail(String email) {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found user by email"));
        return user.getId().toString();
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param authentication the user's details used to generate the token.
     * @param id the user's unique identifier.
     * @return a signed JWT token.
     */
    @Override
    @Transactional(readOnly = true)
    public String getJWTToken(UserDetailsImpl authentication, long id) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(180, MINUTES)) // Token valid for 3 hours
                .subject(String.valueOf(id))
                .claim("scope", createScope(authentication))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Creates the scope for the JWT token by concatenating the user's authorities.
     *
     * @param authentication the user's details containing their authorities.
     * @return a space-separated string of authorities.
     */
    @Override
    @Transactional(readOnly = true)
    public String createScope(UserDetailsImpl authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
