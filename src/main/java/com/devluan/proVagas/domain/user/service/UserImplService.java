package com.devluan.proVagas.domain.user.service;

import com.devluan.proVagas.application.dto.user.request.LoginUserRequest;
import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.LoginUserResponse;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.application.service.user.UserApplicationService;
import com.devluan.proVagas.domain.user.mapper.UserMapper;
import com.devluan.proVagas.domain.user.model.User;
import com.devluan.proVagas.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserImplService implements UserApplicationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Override
    public UserRegisterResponse createUser(UserRegisterRequest request) {
        if(request == null) {
            log.error("Tentativa de criar usuário com request nulo.");
            throw new IllegalArgumentException("O usuário não pode ser nulo.");
        }
        try {
            request.validate();
        } catch (IllegalArgumentException e) {
            log.warn("Validação falhou ao criar usuário: {}", e.getMessage());
            throw e;
        }
        log.info("Criando novo usuário com email: {}", request.email());
        User newUser = userMapper.toEntity(request);
        String encondedPassword = passwordEncoder.encode(request.password());
        newUser.changePassword(encondedPassword);
        userRepository.save(newUser);
        log.info("Usuário criado com sucesso: {}", newUser.getId());
        return userMapper.toResponse(newUser);
    }

    @Override
    public void updateUser(UUID userId, UserRegisterRequest request) {
        if (userId == null) {
            log.error("Tentativa de atualizar usuário com ID nulo.");
            throw new IllegalArgumentException("ID do usuário não pode ser nulo.");
        }
        if (request == null) {
            log.error("Tentativa de atualizar usuário com request nulo.");
            throw new IllegalArgumentException("Request não pode ser nulo.");
        }
        
        try {
        } catch (IllegalArgumentException e) {
            log.warn("Validação falhou ao atualizar usuário: {}", e.getMessage());
            throw e;
        }
        
        log.info("Atualizando usuário com ID: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado para atualização: {}", userId);
                    return new IllegalArgumentException("Usuário não encontrado.");
                });
        
        String updatedName = (request.name() != null && !request.name().isBlank()) ? request.name() : existingUser.getName();
        String updatedEmail = (request.email() != null && !request.email().isBlank()) ? request.email() : existingUser.getEmail();
        String updatedPassword = (request.password() != null && !request.password().isBlank()) ? request.password() : existingUser.getPassword();
        
        User updatedUser = new User(
            existingUser.getId(),
            updatedName,
            updatedEmail,
            updatedPassword,
            existingUser.getCreatedAt(),
            existingUser.isActive()
        );
        
        userRepository.save(updatedUser);
        log.info("Usuário atualizado com sucesso: {}", userId);
    }

    @Override
    public Optional<User> findUserById(UUID userId) {
        if (userId == null) {
            log.error("Tentativa de buscar usuário com ID nulo.");
            return Optional.empty();
        }
        
        log.info("Buscando usuário com ID: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        
        if (user.isPresent()) {
            log.info("Usuário encontrado: {}", userId);
        } else {
            log.warn("Usuário não encontrado: {}", userId);
        }
        
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        if (userId == null) {
            log.error("Tentativa de deletar usuário com ID nulo.");
            throw new IllegalArgumentException("ID do usuário não pode ser nulo.");
        }
        
        log.info("Deletando usuário com ID: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado para deleção: {}", userId);
                    return new IllegalArgumentException("Usuário não encontrado.");
                });
        
        userRepository.delete(existingUser);
        log.info("Usuário deletado com sucesso: {}", userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public LoginUserResponse authenticateAccount(LoginUserRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Attempt to login with unregistered email: {}", request.email());
                    return new IllegalArgumentException("E-mail not found.");
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Login attempt with incorrect password for user: {}", request.email());
            throw new IllegalArgumentException("Password incorrect.");
        }

        try {
            var now = Instant.now();
            var expiresIn = 300L;

            var claims = JwtClaimsSet.builder()
                    .issuer("proVagas-service")
                    .subject(user.getEmail())
                    .expiresAt(now.plusSeconds(expiresIn))
                    .build();

            var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            log.info("Successful login for user: {}", request.email());
            return new LoginUserResponse(jwtValue, expiresIn);
            
        } catch (RuntimeException e) {
            log.error("Unexpected error during JWT encoding: {}", e.getMessage());
            throw new IllegalArgumentException("Unexpected error during authentication.");
        }
    }
}
