package example.model;


import example.auth.PasswordDigest;
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
}
