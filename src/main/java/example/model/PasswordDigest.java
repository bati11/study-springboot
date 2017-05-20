package example.model;

import example.MessageDigestUtil;
import lombok.Getter;

@Getter
public class PasswordDigest {
    private String value;

    public PasswordDigest(String rawPassword) {
        this.value = MessageDigestUtil.sha256(rawPassword);
    }
}
