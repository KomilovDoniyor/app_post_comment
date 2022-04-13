package test.app_post_comment.service;

import org.springframework.http.ResponseEntity;
import test.app_post_comment.domain.User;
import test.app_post_comment.dto.AuthenticationResponse;
import test.app_post_comment.dto.LoginRequest;
import test.app_post_comment.dto.RefreshTokenRequest;
import test.app_post_comment.dto.RegisterRequest;

public interface AuthService {

    /**
     * REGISTER USER
     * @param request
     */
    void register(RegisterRequest request);

    /**
     * SEND TOKEN TO ACCOUNT
     * @param token
     */
    void verifyAccount(String token);

    /**
     * USER LOGIN
     * @param request
     * @return
     */
    ResponseEntity<AuthenticationResponse> login(LoginRequest request);

    /**
     * REFRESH TOKEN
     * @param request
     * @return
     */
    ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest request);

    /**
     * DELETE REFRESH TOKEN
     * @param refreshToken
     * @return
     */
    ResponseEntity<String> logout(String refreshToken);

    User getCurrentUser();

    boolean isLoggedIn();
}
