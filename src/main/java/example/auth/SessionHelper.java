package example.auth;

import example.model.LoginAccount;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Component
public class SessionHelper {
    private LoginAccountRepository loginAccountRepository;

    public SessionHelper(LoginAccountRepository loginAccountRepository) {
        this.loginAccountRepository = loginAccountRepository;
    }

    public Optional<LoginAccount> currentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null) && (authentication.getPrincipal() instanceof LoginAccount)) {
            return Optional.of((LoginAccount) authentication.getPrincipal());
        } else {
            return Optional.empty();
        }
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null) && (authentication.getPrincipal() instanceof LoginAccount)) {
            LoginAccount loginAccount = (LoginAccount) authentication.getPrincipal();
            if (CollectionUtils.isEmpty(loginAccount.getAuthorities())) {
                return false;
            } else {
                return loginAccount.getAuthorities().stream().anyMatch((x) -> x.getAuthority().equals("ADMIN"));
            }
        } else {
            return false;
        }
    }

    public boolean isLoginUser(Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null) && (authentication.getPrincipal() instanceof LoginAccount)) {
            LoginAccount loginAccount = (LoginAccount) authentication.getPrincipal();
            return loginAccount.getUserId().equals(userId);
        } else {
            return false;
        }
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) && (authentication.getPrincipal() instanceof LoginAccount);
    }

    public void login(LoginAccount loginAccount) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginAccount, "", loginAccount.getAuthorities());
        authenticationToken.eraseCredentials();
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
