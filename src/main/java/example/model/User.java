package example.model;


import lombok.Getter;
import lombok.NonNull;

@Getter
public class User {
    private int id;
    private String name;
    private String email;
    private PasswordDigest passwordDigest;

    public User(@NonNull String name, @NonNull String email, @NonNull PasswordDigest passwordDigest) {
        this.name = name;
        this.email = email;
        this.passwordDigest = passwordDigest;
    }

    public boolean authenticate(String rawPassword) {
        try {
            return passwordDigest.equals(new PasswordDigest(rawPassword));
        } catch (NullPointerException e) {
            return false;
        }
    }
}
