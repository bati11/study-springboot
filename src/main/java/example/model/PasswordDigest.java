package example.model;

import example.MessageDigestUtil;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class PasswordDigest {
    private String value;

    private PasswordDigest() {}

    public static PasswordDigest fromDigest(@NonNull String digest) {
        PasswordDigest self = new PasswordDigest();
        self.value = digest;
        return self;
    }

    public PasswordDigest(@NonNull String rawPassword) {
        this.value = MessageDigestUtil.sha256(rawPassword);
    }
}
