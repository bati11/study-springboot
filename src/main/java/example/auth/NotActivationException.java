package example.auth;

import org.springframework.security.core.AuthenticationException;

public class NotActivationException extends AuthenticationException {
    public NotActivationException(String msg) {
        super(msg);
    }
}
