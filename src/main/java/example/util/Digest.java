package example.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Digest {

    @Getter
    private String value;

    Digest(String value) {
        this.value = value;
    }
}
