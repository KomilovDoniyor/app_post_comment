package test.app_post_comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.app_post_comment.dto.AuthenticationResponse;
import test.app_post_comment.dto.LoginRequest;
import test.app_post_comment.dto.RefreshTokenRequest;
import test.app_post_comment.dto.RegisterRequest;
import test.app_post_comment.service.AuthService;

import javax.validation.Valid;
import java.util.Scanner;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.ok("Successfully user registered");
    }

    @GetMapping("/accountVerification/{verification_token}")
    public HttpEntity<?> accountVerification(@PathVariable("verification_token") String token){
        authService.verifyAccount(token);
        return ResponseEntity.ok("Congratulations!\n" +
                "Account has been successfully activated! ");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest request){
        return authService.logout(request.getRefreshToken());
    }
}
