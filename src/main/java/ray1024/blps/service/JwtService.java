package ray1024.blps.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ray1024.blps.model.entity.RefreshToken;
import ray1024.blps.model.entity.User;
import ray1024.blps.model.response.TokenResponse;
import ray1024.blps.repository.RefreshTokenRepository;
import ray1024.blps.repository.UserRepository;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    @Value("${jwt.token.expire-time}")
    private long jwtExpiration;

    @Value("${jwt.token.refresh-time}")
    private long refreshTokenDuration;


    public String newJwtToken(@NonNull String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtExpiration)).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
    }

    public String extractUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isJwtTokenValid(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Transactional
    public RefreshToken newRefreshToken(String username) {
        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(userRepository.findByUsername(username).orElseThrow())
                        .expiryDate(Instant.now().plusMillis(refreshTokenDuration))
                        .token(UUID.randomUUID().toString())
                        .build()
        );
    }

    @Transactional
    public TokenResponse updateRefreshTokenByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        RefreshToken token = refreshTokenRepository.findByUser(user).orElseThrow();
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            token = newRefreshToken(username);
            refreshTokenRepository.save(token);
        }
        return TokenResponse.builder()
                .token(newJwtToken(username))
                .refreshToken(token.getToken())
                .build();
    }

    @Transactional
    public TokenResponse updateRefreshTokenByToken(@NonNull String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow();
        User user = token.getUser();
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            token = newRefreshToken(user.getUsername());
            refreshTokenRepository.save(token);
        }
        return TokenResponse.builder()
                .token(newJwtToken(user.getUsername()))
                .refreshToken(token.getToken())
                .build();
    }
}