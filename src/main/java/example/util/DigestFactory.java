package example.util;

import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

public class DigestFactory {

    public static Digest fromDigest(@NonNull String digest) {
        Digest result = new Digest(digest);
        return result;
    }

    public static Digest create(@NonNull String rawPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String value = bCryptPasswordEncoder.encode(rawPassword);
        Digest result = new Digest(value);
        return result;
    }
}
