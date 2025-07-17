package com.devluan.proVagas.domain.user.service;

import com.devluan.proVagas.application.service.user.RoleService;
import com.devluan.proVagas.domain.user.model.ApplicationRole;
import com.devluan.proVagas.domain.user.model.Role;
import com.devluan.proVagas.domain.user.repository.RoleRepository;
import com.devluan.proVagas.infrastructure.logging.LoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final LoggerService logger;

    @Override
    @Transactional
    public Role getRole(ApplicationRole applicationRole) {
        return roleRepository.findByName(applicationRole.name())
                .orElseGet(() -> {
                    logger.warn("Role '{}' não encontrada no banco de dados, criando-a.", applicationRole.name());
                    return roleRepository.save(new Role(applicationRole));
                });
    }

    @Override
    @Transactional
    public Set<Role> getRoles(Set<ApplicationRole> applicationRoles) {
        return applicationRoles.stream()
                .map(this::getRole)
                .collect(Collectors.toSet());
    }
}
