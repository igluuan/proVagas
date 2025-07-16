package com.devluan.proVagas.application.dto.user.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.devluan.proVagas.domain.user.model.Role;

public record MyProfileUserResponse(
    String name,
    String email,
    LocalDateTime createdAt,
    Set<Role> role_user,
    boolean isActive
) {
    
}
