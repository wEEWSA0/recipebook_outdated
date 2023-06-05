package com.weewsa.recipebookv2.refreshToken;

import com.weewsa.recipebookv2.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);
    Optional<RefreshToken> findRefreshTokenByUserId(Long userId);
    Optional<Long> findUserIdByToken(String token);
}
