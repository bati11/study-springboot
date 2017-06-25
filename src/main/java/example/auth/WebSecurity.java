package example.auth;

import example.model.LoginAccount;
import org.springframework.security.core.Authentication;

public class WebSecurity {
    public boolean checkUserId(Authentication authentication, int id) {
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof LoginAccount) {
            return ((LoginAccount) authentication.getPrincipal()).getUserId().equals(id);
        } else {
            return false;
        }
    }
}
