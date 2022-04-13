package test.app_post_comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.app_post_comment.domain.auth.RefreshToken;
import test.app_post_comment.dto.RefreshTokenRequest;
import test.app_post_comment.exception.RefreshTokenException;
import test.app_post_comment.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateToken(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).
                orElseThrow(() ->new RefreshTokenException("Invalid Refresh Token"));
    }

    public void delete(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).
                orElseThrow(() -> new RefreshTokenException("Refresh token not found"));
        refreshTokenRepository.delete(token);
    }
}
