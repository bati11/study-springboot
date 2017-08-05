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
    public void send(String to, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            setMessageInfo(messageHelper, to, subject, text);

            System.out.println("==========");
            System.out.println("FROM: " + Arrays.toString(messageHelper.getMimeMessage().getFrom()));
            System.out.println("TO: " + Arrays.toString(messageHelper.getMimeMessage().getRecipients(Message.RecipientType.TO)));
            System.out.println("SUBJECT: " + messageHelper.getMimeMessage().getSubject());
            System.out.println();
            MimeMessage mimeMessage = messageHelper.getMimeMessage();
            MimeMultipart mmp = (MimeMultipart) mimeMessage.getContent();
            for (int i = 0; i < mmp.getCount(); i++) {
                System.out.println(getText(mmp.getBodyPart(i)));
            }

            System.out.println("==========");
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getText(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            return StreamUtils.copyToString(part.getInputStream(), StandardCharsets.UTF_8);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart)part.getContent();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < multipart.getCount(); i++) {
                Part bodyPart = multipart.getBodyPart(i);
                sb.append(getText(bodyPart));
                sb.append("\n");
            }
            return sb.toString();
        }
        return null;
    }
}
