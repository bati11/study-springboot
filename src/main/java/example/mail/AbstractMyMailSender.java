package example.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public abstract class AbstractMyMailSender implements MyMailSender {

    protected MimeMessage createMessage(JavaMailSender javaMailSender, String to, String subject, String text) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

        setMessageInfo(messageHelper, to, subject, text);

        return message;
    }

    protected void setMessageInfo(MimeMessageHelper messageHelper, String to, String subject, String text) throws MessagingException {
        messageHelper.setFrom("info@example.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(text);
    }
}
