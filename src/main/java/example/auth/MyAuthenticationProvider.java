package example.auth;

import example.model.LoginAccount;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticate = super.authenticate(authentication);

        if (authenticate.getPrincipal() instanceof LoginAccount) {
            LoginAccount loginAccount = (LoginAccount)authenticate.getPrincipal();
            if (loginAccount.getActivated()) {
                return authenticate;
            } else {
                throw new NotActivationException("not activated");
            }
        } else {
            throw new RuntimeException("authenticate object is not LoginAccount.");
        }
    }
}
