package example.model;

import org.springframework.security.crypto.password.PasswordEncoder;

public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return PasswordDigest.create(rawPassword.toString()).getValue();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return PasswordDigest.create(rawPassword.toString()).getValue().equals(encodedPassword);
    }
}
