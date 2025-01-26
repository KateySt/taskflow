package org.taskflow.com.service;

import java.util.Map;

public interface TokenCacheService {
    void saveToken(String token, String email);

    String getEmailByToken(String token);

    void deleteToken(String token);

    Map<String, String> getAllTokens();
}
