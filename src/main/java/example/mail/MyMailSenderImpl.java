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
    public void send(String to, String subject, String text) {
        try {
            MimeMessage message = createMessage(javaMailSender, to, subject, text);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
