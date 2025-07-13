package com.devluan.proVagas.domain.user.service;

import com.devluan.proVagas.domain.user.model.ApplicationRole;
import com.devluan.proVagas.domain.user.model.Role;

import java.util.Set;

public interface RoleService {
    Role getRole(ApplicationRole applicationRole);
    Set<Role> getRoles(Set<ApplicationRole> applicationRoles);
}
