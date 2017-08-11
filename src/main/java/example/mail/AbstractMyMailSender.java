package example.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public abstract class AbstractMyMailSender implements MyMailSender {

    protected MimeMessage createMessage(JavaMailSender javaMailSender, MailParam mailParam) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

        setMessageInfo(messageHelper, mailParam);

        return message;
    }

    protected void setMessageInfo(MimeMessageHelper messageHelper, MailParam mailParam) throws MessagingException {
        messageHelper.setFrom(mailParam.getFrom());
        messageHelper.setTo(mailParam.getTo());
        messageHelper.setSubject(mailParam.getSubject());
        messageHelper.setText(mailParam.getMailContent());
    }
}
