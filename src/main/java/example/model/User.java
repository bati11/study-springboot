package example.model;


import example.util.Digest;
import example.util.DigestFactory;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Getter
public class User {
    private Integer id;
    private String name;
    private String email;
    private Digest passwordDigest;
    private Boolean activated;
    private Digest activationDigest;

    private User () {}

    public static User from(
            @NonNull Integer id,
            @NonNull String name,
            @NonNull String email,
            @NonNull Boolean activated,
            @NonNull String activationDigest
    ) {
        User result = new User();
        result.id = id;
        result.name = name;
        result.email = email;
        result.activated = activated;
        result.activationDigest = DigestFactory.fromDigest(activationDigest);
        return result;
    }

    public static User create(
            @NonNull String name,
            @NonNull String email,
            @NonNull String rawPassword,
            @NonNull String activationToken
    ) {
        Digest passwordDigest = DigestFactory.create(rawPassword);
        Digest activationDigest = DigestFactory.create(activationToken);

        User result = new User();
        result.name = name;
        result.email = email.toLowerCase();
        result.passwordDigest = passwordDigest;
        result.activated = false;
        result.activationDigest = activationDigest;
        return result;
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

    public boolean authenticatedActivation(String activationToken) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(activationToken, this.activationDigest.getValue());
    }
}
