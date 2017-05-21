package example.model;


import lombok.Getter;

@Getter
public class User {
    private int id;
    private String name;
    private String email;
    private PasswordDigest passwordDigest;

    public User(String name, String email, PasswordDigest passwordDigest) {
        this.name = name;
        this.email = email;
        this.passwordDigest = passwordDigest;
    }

    public boolean authenticate(String rawPassword) {
        return passwordDigest.equals(new PasswordDigest(rawPassword));
    }
}
