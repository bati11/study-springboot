package example.auth;

import example.MessageDigestUtil;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PasswordDigestFactory {

    private final String salt= "SALT";

    public PasswordDigest fromDigest(@NonNull String digest) {
        PasswordDigest result = new PasswordDigest(digest);
        return result;
    }

    public PasswordDigest create(@NonNull String rawPassword) {
        String value = MessageDigestUtil.sha256(rawPassword + salt);
        PasswordDigest result = new PasswordDigest(value);
        return result;
    }
}
