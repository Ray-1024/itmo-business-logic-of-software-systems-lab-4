package ray1024.blps.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ray1024.blps.model.entity.User;
import ray1024.blps.service.JwtService;
import ray1024.blps.service.UserService;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@Component
public class JwtJAASFilter extends OncePerRequestFilter {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String JWT_AUTHORIZATION_PREFIX = "Bearer ";

    private JwtService jwtService;

    private UserService userService;

    private AbstractJaasAuthenticationProvider authenticationProvider;

    private static final Logger logger = LoggerFactory.getLogger(JwtJAASFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> token = parseJwt(request);
            if (token.isPresent() && jwtService.isJwtTokenValid(token.get())) {
                String username = jwtService.extractUsernameFromJwtToken(token.get());

                User user = userService.loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(JWT_AUTHORIZATION_PREFIX)) {
            return Optional.of(headerAuth.substring(JWT_AUTHORIZATION_PREFIX.length(), headerAuth.length()));
        }

        return Optional.empty();
    }

}