package example.auth;

import org.springframework.security.crypto.password.PasswordEncoder;

public class MyPasswordEncoder implements PasswordEncoder {

    private PasswordDigestFactory passwordDigestFactory;

    public MyPasswordEncoder(PasswordDigestFactory passwordDigestFactory) {
        this.passwordDigestFactory = passwordDigestFactory;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordDigestFactory.create(rawPassword.toString()).getValue();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordDigestFactory.create(rawPassword.toString()).getValue().equals(encodedPassword);
    }
}
