package com.devluan.proVagas.domain.password.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.devluan.proVagas.application.service.password.PasswordResetTokenService;
import com.devluan.proVagas.domain.password.model.PasswordResetToken;
import com.devluan.proVagas.domain.password.repository.PasswordResetTokenRepository;
import com.devluan.proVagas.domain.user.exception.UserNotFoundException;
import com.devluan.proVagas.domain.user.model.User;
import com.devluan.proVagas.domain.user.repository.UserRepository;
import com.devluan.proVagas.infrastructure.email.service.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PasswordResetTokenImpl implements PasswordResetTokenService{

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private static final int TOKEN_EXPIRATION_HOURS = 1;

    @Override
    public void requestPasswordReset(String email) {
        if(email == null){
            throw new NullPointerException("Email não pode ser nulo.");
        }
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        tokenRepository.deleteByUser(user.getId());

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS));
        resetToken.setCreatedAt(LocalDateTime.now());
        
        tokenRepository.save(resetToken);

        String baseUrl = System.getProperty("app.base-url", "http://localhost:8080");
        String resetLink = baseUrl + "/reset-password?token=" + token;
        String emailBody = "Para redefinir sua senha, clique no link: " + resetLink;
        emailService.sendEmail(user.getEmail(), "Redefinição de Senha", emailBody);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        if (!isValidToken(token)) {
            throw new IllegalArgumentException("Token inválido ou expirado.");
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(token).get();
        User user = resetToken.getUser();

        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }

    @Override
    public boolean isValidToken(String token) {
        return tokenRepository.findByToken(token)
                .map(resetToken -> !resetToken.isExpired())
                .orElse(false);
    }
}