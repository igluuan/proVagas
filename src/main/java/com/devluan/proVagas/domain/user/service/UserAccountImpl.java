package com.devluan.proVagas.domain.user.service;

import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.service.user.UserAccountService;
import com.devluan.proVagas.domain.user.model.User;
import com.devluan.proVagas.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.devluan.proVagas.infrastructure.logging.LoggerService;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAccountImpl implements UserAccountService {

    private final UserRepository userRepository;
    private final LoggerService logger;
    
    @Override
    public void updateMyProfile(UUID userId, UserRegisterRequest request) {
        if (userId == null) {
            logger.error("Tentativa de atualizar usuário com ID nulo.");
            throw new IllegalArgumentException("ID do usuário não pode ser nulo.");
        }
        if (request == null) {
            logger.error("Tentativa de atualizar usuário com request nulo.");
            throw new IllegalArgumentException("Request não pode ser nulo.");
        }
        
        try {
        } catch (IllegalArgumentException e) {
            logger.warn("Validação falhou ao atualizar usuário: {}", e.getMessage());
            throw e;
        }
        
        logger.info("Atualizando usuário com ID: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado para atualização: {}", userId);
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
            existingUser.isActive(),
            existingUser.getRoles()
            );
        
        userRepository.save(updatedUser);
        logger.info("Usuário atualizado com sucesso: {}", userId);
    }

    @Override
    public void deleteMyAccount(UUID userId) {
        if (userId == null) {
            logger.error("Tentativa de deletar usuário com ID nulo.");
            throw new IllegalArgumentException("ID do usuário não pode ser nulo.");
        }
        
        logger.info("Deletando usuário com ID: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado para deleção: {}", userId);
                    return new IllegalArgumentException("Usuário não encontrado.");
                });
        
        userRepository.delete(existingUser);
        logger.info("Usuário deletado com sucesso: {}", userId);
    }

    @Override
    public User getMyProfile(Jwt jwt) {
        if (jwt == null) {
            throw new IllegalArgumentException("Token não pode ser nulo");
        }
        String email = jwt.getSubject();
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Token não pode ser nulo");
        }
        return userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }
}
