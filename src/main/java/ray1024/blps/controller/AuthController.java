package ray1024.blps.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ray1024.blps.exception.UserAlreadyExistsException;
import ray1024.blps.model.entity.Role;
import ray1024.blps.model.request.RefreshTokenRequest;
import ray1024.blps.model.request.SignUpRequest;
import ray1024.blps.model.response.TokenResponse;
import ray1024.blps.service.AuthService;

@AllArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/auth")
    public TokenResponse signUp(@RequestBody SignUpRequest signupRequest) throws UserAlreadyExistsException {
        return authService.signUp(signupRequest.getUsername(), signupRequest.getPassword(), Role.valueOf(signupRequest.getRole()));
    }

    @PutMapping("/api/auth/token")
    public TokenResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refresh(refreshTokenRequest.getToken());
    }
}

