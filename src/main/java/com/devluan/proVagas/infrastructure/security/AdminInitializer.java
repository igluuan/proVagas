package com.devluan.proVagas.infrastructure.security;

import com.devluan.proVagas.domain.user.model.Role;
import com.devluan.proVagas.domain.user.model.User;
import com.devluan.proVagas.domain.user.repository.UserRepository;
import com.devluan.proVagas.domain.user.service.RoleService;
import com.devluan.proVagas.infrastructure.logging.LoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.devluan.proVagas.domain.user.model.ApplicationRole;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import com.devluan.proVagas.domain.user.model.Role;

@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(AdminInitializer.class)
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final LoggerService logger;

    @Value("${app.security.admin.username}")
    private String adminUsername;

    @Value("${app.security.admin.password}")
    private String adminPassword;

    @Value("${app.security.admin.email}")
    private String adminEmail;

    @Value("${app.security.admin.roles}")
    private String adminRoles;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            logger.info("Criando usuário administrador inicial...");

            Set<ApplicationRole> adminApplicationRoles = Arrays.stream(adminRoles.split(","))
                    .map(String::trim)
                    .map(ApplicationRole::valueOf)
                    .collect(Collectors.toSet());

            Set<Role> roles = roleService.getRoles(adminApplicationRoles);

            User adminUser = new User(
                    null,
                    adminUsername,
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    null,
                    true,
                    roles
            );
            userRepository.save(adminUser);
            logger.info("Usuário administrador '{}' criado com sucesso.", adminUsername);
        } else {
            logger.info("Usuário administrador '{}' já existe. Pulando a criação.", adminUsername);
        }
    }
}
