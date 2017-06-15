package example.auth;

import example.model.LoginAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionHelper {
    public Optional<LoginAccount> currentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null) && (authentication.getPrincipal() instanceof LoginAccount)) {
            return Optional.of((LoginAccount) authentication.getPrincipal());
        } else {
            return Optional.empty();
        }
    }
}
