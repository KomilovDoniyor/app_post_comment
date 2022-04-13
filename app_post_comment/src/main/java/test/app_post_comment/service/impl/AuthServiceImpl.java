package test.app_post_comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.app_post_comment.domain.User;
import test.app_post_comment.domain.auth.VerificationToken;
import test.app_post_comment.dto.*;
import test.app_post_comment.repository.UserRepository;
import test.app_post_comment.repository.VerificationTokenRepository;
import test.app_post_comment.security.JwtProvider;
import test.app_post_comment.service.AuthService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.expiration.time}")
    private Long expireMilliSeconds;
    @Value("${server.port}")
    private int portServer;


    @Override
    public void register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setEnable(false);

        User savedUser = userRepository.save(user);

        String token = generateVerificationToken(savedUser);
        mailService.send(new NotificationEmail(
                "Iltimos accountigizni activatsiya qiling",
                user.getEmail(),
                "Ushbu link orqali: " + "http://localhost:" + portServer + "/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User savedUser) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS));
        verificationTokenRepository.save(verificationToken);
        return token;
    }

//    private String generateVerificationToken(User user) {
//        String token = UUID.randomUUID().toString();
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setToken(token);
//        verificationToken.setUser(user);
//        verificationToken.setExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS));
//        verificationTokenRepository.save(verificationToken);
//        return token;
//    }

    @Override
    public void verifyAccount(String token) {
        Optional<VerificationToken> optionalVerificationToken = verificationTokenRepository.findByToken(token);
        if (optionalVerificationToken.isPresent()) {
            VerificationToken verificationToken = optionalVerificationToken.get();
            if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
                log.error("Verification token expired");
                throw new RuntimeException("Verification token expired");
            }
            User user = verificationToken.getUser();
            user.setEnable(true);
            userRepository.save(user);
        }
    }

    @Override
    public ResponseEntity<AuthenticationResponse> login(LoginRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String authenticationToken =
                jwtProvider.
                        generateToken((org.springframework.security.core.userdetails.User) authenticate.getPrincipal());

        AuthenticationResponse response =
                AuthenticationResponse.builder()
                        .authenticationToken(authenticationToken)
                        .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                        .username(request.getUsername())
                        .expiryDate(Instant.now().plusMillis(expireMilliSeconds))
                        .build();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest request) {
        refreshTokenService.validateToken(request.getRefreshToken());
        String authenticationToken = jwtProvider.generateTokenWithUser(request.getUsername());
        AuthenticationResponse response =
                AuthenticationResponse.builder()
                        .authenticationToken(authenticationToken)
                        .refreshToken(request.getRefreshToken())
                        .username(request.getUsername())
                        .expiryDate(Instant.now().plusMillis(expireMilliSeconds))
                        .build();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> logout(String refreshToken) {
        refreshTokenService.delete(refreshToken);
        return ResponseEntity.status(200).body("Successfully delete refresh token!!!");
    }

    @Transactional(readOnly = true)
    @Override
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + principal.getSubject()));
    }

    @Override
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated();
    }
}
