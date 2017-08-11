package example.mail;

import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MyMailSenderImpl extends AbstractMyMailSender {

    private JavaMailSender javaMailSender;

    public MyMailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public MimeMessage createMimeMessage(MailParam mailParam) {
        try {
            return createMessage(javaMailSender, mailParam);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(MimeMessage mimeMessage) {
        javaMailSender.send(mimeMessage);
    }
}
