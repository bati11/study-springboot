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
    public void send(MailParam mailParam) {
        try {
            MimeMessage message = createMessage(javaMailSender, mailParam);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
