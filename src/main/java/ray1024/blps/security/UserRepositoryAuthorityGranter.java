package ray1024.blps.security;

import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.GrantedAuthority;
import ray1024.blps.model.entity.User;
import ray1024.blps.repository.UserRepository;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


public class UserRepositoryAuthorityGranter implements AuthorityGranter {

    private final UserRepository userRepository;

    public UserRepositoryAuthorityGranter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<String> grant(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        return new HashSet<>(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }
}