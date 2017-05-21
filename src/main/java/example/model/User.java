package example.model;


import lombok.Getter;
import lombok.NonNull;

@Getter
public class User {
    private Integer id;
    private String name;
    private String email;
    private PasswordDigest passwordDigest;

    private User () {}

    public static User from(
            @NonNull Integer id,
            @NonNull String name,
            @NonNull String email,
            @NonNull PasswordDigest passwordDigest
    ) {
        User result = new User();
        result.id = id;
        result.name = name;
        result.email = email;
        result.passwordDigest = passwordDigest;
        return result;
    }

    public static User create(@NonNull String name, @NonNull String email, @NonNull PasswordDigest passwordDigest) {
        User result = new User();
        result.name = name;
        result.email = email;
        result.passwordDigest = passwordDigest;
        return result;
    }

    public boolean authenticate(String rawPassword) {
        try {
            return passwordDigest.equals(PasswordDigest.create(rawPassword));
        } catch (NullPointerException e) {
            return false;
        }
    }
}
