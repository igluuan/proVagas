package com.devluan.proVagas.domain.user.service;

import com.devluan.proVagas.application.service.user.TokenBlocklistService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlocklistServiceImpl implements TokenBlocklistService {

    private final Cache<String, String> cache;

    public TokenBlocklistServiceImpl() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    @Override
    public void add(String token) {
        cache.put(token, "");
    }

    @Override
    public boolean isBlocklisted(String token) {
        return cache.getIfPresent(token) != null;
    }
}
