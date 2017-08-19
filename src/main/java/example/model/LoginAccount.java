package example.model;

import example.util.Digest;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;

public class LoginAccount implements UserDetails {

    private final Integer userId;
    private final String username;
    private final String password;
    private final Collection<GrantedAuthority> authorities;

    @Getter
    private Boolean isAdmin;

    @Getter
    private Boolean activated;

    @Getter
    private Digest activationDigest;

    @Getter
    private Digest resetDigest;

    @Getter
    private Instant resetSentAt;

    public LoginAccount(
            Integer userId,
            String email,
            String passwordDigest,
            boolean isAdmin,
            boolean activated,
            Digest activationDigest,
            Digest resetDigest,
            Instant resetSentAt
    ) {
        this.userId = userId;
        this.username = email;
        this.password = passwordDigest;
        this.isAdmin = isAdmin;
        this.activated = activated;
        this.activationDigest = activationDigest;
        this.resetDigest = resetDigest;
        this.resetSentAt = resetSentAt;

        if (isAdmin) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ADMIN");
            List<GrantedAuthority> l = new ArrayList<>();
            l.add(grantedAuthority);
            this.authorities = l;
        } else {
            this.authorities = null;
        }
    }

    public static String newToken() {
        byte[] bytes = new byte[20];
        try {
            SecureRandom.getInstanceStrong().nextBytes(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return Base64.encodeBase64URLSafeString(bytes);
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return username;
    }

    public boolean authenticatedActivation(String activationToken) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(activationToken, this.activationDigest.getValue());
    }

    public boolean authenticatedReset(String resetToken) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(resetToken, this.resetDigest.getValue());
    }

    public boolean isResetExpiration() {
        return Instant.now().isAfter(resetSentAt.plus(2, HOURS));
    }
}
