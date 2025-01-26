package org.taskflow.com.annotation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.taskflow.com.service.TokenCacheService;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenValidationAspect {

    private final TokenCacheService tokenCacheService;

    @Around("@annotation(CheckToken)")
    public Object checkTokenAndEmail(ProceedingJoinPoint joinPoint) throws Throwable {
        String authHeader = null;

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof String && arg.toString().startsWith("Bearer ")) {
                authHeader = (String) arg;
                break;
            }
        }

        if (authHeader == null) {
            throw new EntityNotFoundException("Authorization header is missing");
        }

        String email = tokenCacheService.getEmailByToken(authHeader);

        if (email == null) {
            throw new EntityNotFoundException("User email not found in cache");
        }

        log.info("Email {} found in cache. Proceeding with the request.", email);

        return joinPoint.proceed();
    }
}