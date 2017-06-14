package example.auth;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class PasswordDigest {

    @Getter
    private String value;

    PasswordDigest(String value) {
        this.value = value;
    }
}
