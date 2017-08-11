package example.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StreamUtils;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DebugMailSenderImpl extends AbstractMyMailSender {
    private JavaMailSender javaMailSender;

    public DebugMailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public MimeMessage createMimeMessage(MailParam mailParam) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            setMessageInfo(messageHelper, mailParam);
            return message;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(MimeMessage mimeMessage) {
        try {
            System.out.println("==========");
            System.out.println("FROM: " + Arrays.toString(mimeMessage.getFrom()));
            System.out.println("TO: " + Arrays.toString(mimeMessage.getRecipients(Message.RecipientType.TO)));
            System.out.println("SUBJECT: " + mimeMessage.getSubject());
            System.out.println();
            String mailContent = getMailContent(mimeMessage);
            System.out.println(mailContent);

            System.out.println("==========");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
