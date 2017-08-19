package example.util;

import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DigestFactory {

    public static Digest fromDigest(String digest) {
        if (digest == null || digest.equals("")) {
            return null;
        } else {
            Digest result = new Digest(digest);
            return result;
        }
    }

    public static Digest create(@NonNull String rawString) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String value = bCryptPasswordEncoder.encode(rawString);
        Digest result = new Digest(value);
        return result;
    }
}
