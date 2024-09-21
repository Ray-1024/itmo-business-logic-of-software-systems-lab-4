package ray1024.blps.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Map;

public class UserDetailsLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    private String username;
    private boolean isLoginSucceeded;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.userDetailsService = (UserDetailsService) options.get("userDetailsService");
        this.passwordEncoder = (PasswordEncoder) options.get("passwordEncoder");
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCallback = new NameCallback("username");
        PasswordCallback passwordCallback = new PasswordCallback("password", false);

        try {
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            username = nameCallback.getName();
            UserDetails user = userDetailsService.loadUserByUsername(username);
            String password = new String(passwordCallback.getPassword());
            isLoginSucceeded = passwordEncoder.matches(password, user.getPassword());
        } catch (UsernameNotFoundException | IOException | UnsupportedCallbackException e) {
            isLoginSucceeded = false;
        }

        return isLoginSucceeded;
    }

    @Override
    public boolean commit() throws LoginException {
        if (!isLoginSucceeded) {
            return false;
        }

        if (username == null) {
            throw new LoginException("Username is null during the commit");
        }

        Principal principal = (UserPrincipal) () -> username;
        subject.getPrincipals().add(principal);
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        return false;
    }
}