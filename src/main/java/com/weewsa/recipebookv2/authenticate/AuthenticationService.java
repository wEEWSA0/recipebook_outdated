package com.weewsa.recipebookv2.authenticate;

import com.weewsa.recipebookv2.authenticate.dto.AuthenticationRequest;
import com.weewsa.recipebookv2.authenticate.dto.AuthenticationResponse;
import com.weewsa.recipebookv2.authenticate.dto.RegisterRequest;
import com.weewsa.recipebookv2.authenticate.exception.NotAuthorized;
import com.weewsa.recipebookv2.refreshToken.RefreshToken;
import com.weewsa.recipebookv2.refreshToken.RefreshTokenRepository;
import com.weewsa.recipebookv2.refreshToken.exception.InvalidToken;
import com.weewsa.recipebookv2.user.Role;
import com.weewsa.recipebookv2.user.User;
import com.weewsa.recipebookv2.user.UserRepository;
import com.weewsa.recipebookv2.user.exception.UserAlreadyExists;
import com.weewsa.recipebookv2.user.exception.UserNotFound;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    @Value("${application.security.jwt.expiration}")
    private Duration ACCESS_TOKEN_EXPIRATION;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Duration REFRESH_TOKEN_EXPIRATION;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) throws UserAlreadyExists, UserNotFound {
        var isExist = userRepository.existsByLogin(request.getLogin());

        if (isExist) {
            throw new UserAlreadyExists("User already exists");
        }

        User registerUser = User.builder()
                .login(request.getLogin())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .aboutMe(request.getAboutMe())
                .role(Role.USER)
                .build();

        userRepository.save(registerUser);

        return createNewUserTokens(registerUser.getLogin(), registerUser.getRole());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UserNotFound {
        var foundUser = userRepository.findByLogin(request.getLogin());

        if (foundUser.isEmpty() || !passwordEncoder.matches(request.getPassword(), foundUser.get().getPassword())) {
            throw new UserNotFound("User not found");
        }

        var user = foundUser.get();

        return createNewUserTokens(user.getLogin(), user.getRole());
    }

    public AuthenticationResponse authenticateByRefreshToken(String refreshToken) throws InvalidToken, UserNotFound {
        var foundToken = tokenRepository.findRefreshTokenByToken(refreshToken);

        if (foundToken.isEmpty()) {
            throw new InvalidToken("Invalid refresh token");
        }

        var user = foundToken.get().getUser();

        return createNewUserTokens(user.getLogin(), user.getRole());
    }

    private AuthenticationResponse createNewUserTokens(String login, Role role) throws UserNotFound {
        String refreshToken = jwtService.generateRefreshToken(login, REFRESH_TOKEN_EXPIRATION);

        var foundUser = userRepository.findByLogin(login);

        if (foundUser.isEmpty()) {
            throw new UserNotFound("User not found");
        }

        tokenRepository.save(RefreshToken.builder()
                .token(refreshToken)
                .userId(foundUser.get().getId())
//                .user(foundUser.get()) todo разобраться с конфликтом user и userId
                .build());

        Map<String, Object> accessTokenClaims = new HashMap<>();
        accessTokenClaims.put(JWTService.ROLE, role.name());
        String accessToken = jwtService.generateAccessToken(login, accessTokenClaims, ACCESS_TOKEN_EXPIRATION);

        // only test
//        Claims claims;
//        try {
//            claims = jwtService.parseToken(accessToken);
//        } catch (InvalidToken e) {
//            throw new RuntimeException(e);
//        }

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
