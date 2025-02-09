package org.taskflow.com.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TokenCacheServiceImplTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOps;

    @InjectMocks
    private TokenCacheServiceImpl tokenCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(redisTemplate.opsForHash()).thenReturn(hashOps);
    }

    @Test
    void saveToken_ShouldSaveTokenAndEmail() {
        String token = "jwt-token";
        String email = "test@example.com";

        tokenCacheService.saveToken(token, email);

        verify(hashOps, times(1)).put("tokens", email, token);
        verify(redisTemplate, times(1)).opsForHash();
    }

    @Test
    void getEmailByToken_ShouldReturnEmail() {
        String token = "jwt-token";
        String expectedEmail = "test@example.com";

        when(hashOps.get("tokens", token)).thenReturn(expectedEmail);

        String email = tokenCacheService.getEmailByToken(token);

        assertEquals(expectedEmail, email, "The fetched email should match the expected email.");
        verify(hashOps, times(1)).get("tokens", token);
        verify(redisTemplate, times(1)).opsForHash();
    }

    @Test
    void deleteToken_ShouldRemoveToken() {
        String token = "jwt-token";

        tokenCacheService.deleteToken(token);

        verify(hashOps, times(1)).delete("tokens", token);
        verify(redisTemplate, times(1)).opsForHash();
    }
}
