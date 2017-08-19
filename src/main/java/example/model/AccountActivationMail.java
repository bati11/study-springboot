package example.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class AccountActivationMail implements Mail {
    private final String subject;
    private final Map<String, Object> params = new HashMap<>();
    private final String from;
    private final String to;
    private final String contentTemplate;

    public AccountActivationMail(User user, String activationUri) {
        this.subject = "Account activation";
        this.from = "info@example.com";
        this.to = user.getEmail();
        this.contentTemplate = "text/account-activation";

        this.params.put("userName", user.getName());
        this.params.put("activationUri", activationUri);
    }
}
