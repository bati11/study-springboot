package example.model;

import example.MessageDigestUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class PasswordDigest {

    @Getter
    private String value;

    private PasswordDigest() {}

    public static PasswordDigest fromDigest(@NonNull String digest) {
        PasswordDigest result = new PasswordDigest();
        result.value = digest;
        return result;
    }

    public static PasswordDigest create(@NonNull String rawPassword) {
        PasswordDigest result = new PasswordDigest();
        result.value = MessageDigestUtil.sha256(rawPassword);
        return result;
    }
}
