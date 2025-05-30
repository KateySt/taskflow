package org.taskflow.com.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.taskflow.com.service.TokenCacheService;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenCacheServiceImpl implements TokenCacheService {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveToken(String token, String email) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.put("tokens", email, token);
        log.info("Token and email saved: {} -> {}", token, email);
    }

    @Override
    public String getEmailByToken(String token) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String email = hashOps.get("tokens", token);
        log.info("Fetching email for token: {} -> {}", token, email);
        return email;
    }

    @Override
    public void deleteToken(String token) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.delete("tokens", token);
        log.info("Token deleted: {}", token);
    }

    @Override
    public Map<String, String> getAllTokens() {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> tokens = hashOps.entries("tokens");
        log.info("Fetched all tokens: {}", tokens);
        return tokens;
    }
}
