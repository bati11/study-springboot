package example.util;

import lombok.Getter;
import lombok.Value;

@Value
public class Digest {

    @Getter
    private String value;

    Digest(String value) {
        this.value = value;
    }
}
