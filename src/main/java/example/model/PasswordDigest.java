package example.model;

import example.MessageDigestUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PasswordDigest {
    private String value;

    public PasswordDigest(String rawPassword) {
        this.value = MessageDigestUtil.sha256(rawPassword);
    }
}
