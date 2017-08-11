package example.mail;

import javax.mail.internet.MimeMessage;

public interface MyMailSender {
    MimeMessage createMimeMessage(MailParam mailParam);
    String getMailContent(MimeMessage mimeMessage);
    void send(MimeMessage mimeMessage);
}
