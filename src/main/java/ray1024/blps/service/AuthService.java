package ray1024.blps.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.blps.exception.UserAlreadyExistsException;
import ray1024.blps.model.entity.Role;
import ray1024.blps.model.entity.User;
import ray1024.blps.model.response.TokenResponse;
import ray1024.blps.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse signUp(@NonNull String username, @NonNull String password, @NonNull Role role) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(username)) throw new UserAlreadyExistsException();
        userRepository.save(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .role(role)
                        .build()
        );
        return TokenResponse.builder()
                .token(jwtService.newJwtToken(username))
                .refreshToken(jwtService.newRefreshToken(username).getToken())
                .build();
    }

    public TokenResponse refresh(@NonNull String refreshToken) {
        return jwtService.updateRefreshTokenByToken(refreshToken);
    }
}
