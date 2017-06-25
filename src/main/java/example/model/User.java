package example.model;


import lombok.Getter;
import lombok.NonNull;

@Getter
public class User {
    private Integer id;
    private String name;
    private String email;

    private User () {}

    public static User from(
            @NonNull Integer id,
            @NonNull String name,
            @NonNull String email
    ) {
        User result = new User();
        result.id = id;
        result.name = name;
        result.email = email;
        return result;
    }
}
