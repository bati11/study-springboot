package example.model;

import example.MessageDigestUtil;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
public class PasswordDigest {
    private String value;

    public PasswordDigest(@NonNull String rawPassword) {
        this.value = MessageDigestUtil.sha256(rawPassword);
    }
}
