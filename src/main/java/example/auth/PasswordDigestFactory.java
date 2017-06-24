package example.auth;

import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordDigestFactory {

    public PasswordDigest fromDigest(@NonNull String digest) {
        PasswordDigest result = new PasswordDigest(digest);
        return result;
    }

    public PasswordDigest create(@NonNull String rawPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String value = bCryptPasswordEncoder.encode(rawPassword);
        PasswordDigest result = new PasswordDigest(value);
        return result;
    }
}
