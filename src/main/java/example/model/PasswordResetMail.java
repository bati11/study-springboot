package example.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PasswordResetMail implements Mail {
    private final String subject;
    private final Map<String, Object> params = new HashMap<>();
    private final String from;
    private final String to;
    private final String contentTemplate;

    public PasswordResetMail(String toEmail, String resetUri) {
        this.subject = "Password reset";
        this.from = "info@example.com";
        this.to = toEmail;
        this.contentTemplate = "text/password-reset";

        this.params.put("resetUri", resetUri);
    }
}
