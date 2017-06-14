package example.auth;

import example.model.LoginAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SessionHelper {
    public static Optional<LoginAccount> currentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null) && (authentication.getPrincipal() instanceof LoginAccount)) {
            return Optional.of((LoginAccount) authentication.getPrincipal());
        } else {
            return Optional.empty();
        }
    }
}
