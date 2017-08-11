package example.mail;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class MailParam {
    private final String from;
    private final String to;
    private final String subject;
    private final String mailContent;
}
