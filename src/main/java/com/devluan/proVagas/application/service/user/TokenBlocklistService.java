package com.devluan.proVagas.application.service.user;

public interface TokenBlocklistService {
    void add(String token);
    boolean isBlocklisted(String token);
}
