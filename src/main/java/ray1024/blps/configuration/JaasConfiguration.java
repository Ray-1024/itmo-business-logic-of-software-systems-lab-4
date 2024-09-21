package ray1024.blps.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ray1024.blps.repository.UserRepository;
import ray1024.blps.security.UserDetailsLoginModule;
import ray1024.blps.security.UserRepositoryAuthorityGranter;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.Map;

@Configuration
public class JaasConfiguration {
    @Bean
    public AbstractJaasAuthenticationProvider jaasAuthenticationProvider(
            javax.security.auth.login.Configuration configuration,
            UserRepository userRepository) {

        return new DefaultJaasAuthenticationProvider() {{
            setConfiguration(configuration);
            setAuthorityGranters(new AuthorityGranter[]{
                    new UserRepositoryAuthorityGranter(userRepository)
            });
        }};
    }

    @Bean
    public javax.security.auth.login.Configuration configuration(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

        Map<String, Object> options = Map.of(
                "userDetailsService", userDetailsService,
                "passwordEncoder", passwordEncoder
        );

        AppConfigurationEntry entry = new AppConfigurationEntry(
                UserDetailsLoginModule.class.getCanonicalName(),
                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                options
        );

        AppConfigurationEntry[] configurationEntries = {entry};

        return new InMemoryConfiguration(Map.of("SPRINGSECURITY", configurationEntries));
    }
}

