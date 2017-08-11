package example.model;


import example.util.Digest;
import example.util.DigestFactory;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Getter
public class User {
    private Integer id;
    private String name;
    private String email;
    private Digest passwordDigest;
    private String activationToken;

    private User () {}

    public static User from(
            @NonNull Integer id,
            @NonNull String name,
            @NonNull String email,
            @NonNull String activationToken
    ) {
        User result = new User();
        result.id = id;
        result.name = name;
        result.email = email;
        result.activationToken = activationToken;
        return result;
    }

    public static User create(
            @NonNull String name,
            @NonNull String email,
            @NonNull String rawPassword
    ) {
        Digest passwordDigest = DigestFactory.create(rawPassword);
        String activationToken = newToken();

        User result = new User();
        result.name = name;
        result.email = email.toLowerCase();
        result.passwordDigest = passwordDigest;
        result.activationToken = activationToken;
        return result;
    }

    private static String newToken() {
        byte[] bytes = new byte[20];
        try {
            SecureRandom.getInstanceStrong().nextBytes(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return Base64.encodeBase64URLSafeString(bytes);
    }
}
